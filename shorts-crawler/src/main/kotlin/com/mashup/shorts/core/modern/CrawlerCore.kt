package com.mashup.shorts.core.modern

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ofPattern
import org.jsoup.select.Elements
import org.springframework.context.annotation.Primary
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import com.mashup.shorts.KeywordExtractor
import com.mashup.shorts.RankingGenerator
import com.mashup.shorts.consts.CATEGORY_WEIGHT_ONE
import com.mashup.shorts.consts.CATEGORY_WEIGHT_ONE_HALF
import com.mashup.shorts.consts.CATEGORY_WEIGHT_ONE_HALF_QUARTER
import com.mashup.shorts.consts.CATEGORY_WEIGHT_ONE_QUARTER
import com.mashup.shorts.consts.CATEGORY_WEIGHT_TWO_HALF
import com.mashup.shorts.core.modern.consts.categoryToUrl
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.category.CategoryName.CULTURE
import com.mashup.shorts.domain.category.CategoryName.ECONOMIC
import com.mashup.shorts.domain.category.CategoryName.POLITICS
import com.mashup.shorts.domain.category.CategoryName.SCIENCE
import com.mashup.shorts.domain.category.CategoryName.SOCIETY
import com.mashup.shorts.domain.category.CategoryName.WORLD
import com.mashup.shorts.domain.category.CategoryRepository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsBulkInsertRepository
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardBulkInsertRepository
import com.mashup.shorts.util.Slf4j2KotlinLogging.log

@Primary
@Component
class CrawlerCore(
    private val crawlerBase: CrawlerBase,
    private val categoryRepository: CategoryRepository,
    private val newsRepository: NewsRepository,
    private val newsBulkInsertRepository: NewsBulkInsertRepository,
    private val newsCardBulkInsertRepository: NewsCardBulkInsertRepository,
    private val keywordExtractor: KeywordExtractor,
    private val rankingGenerator: RankingGenerator,
) {

    @Retryable(value = [Exception::class], maxAttempts = 3)
    internal fun execute(): LocalDateTime {
        val crawledDateTime = LocalDateTime.now()
        val keywordsCountingPair = mutableMapOf<String, Double>()
        val persistenceTargetNewsCards = mutableListOf<NewsCard>()

        for (categoryPair in categoryToUrl) {
            val categoryName = categoryPair.key
            val categoryURL = categoryPair.value

            log.info { "$categoryName - ${crawledDateTime.format(ofPattern("yyyy-MM-dd HH:mm:ss"))} - crawling start" }

            val currentCategory = getPersistenceCategory(categoryName)
            val headLineLinks = getHeadLineLinks(categoryURL, categoryName)
            val crawledNewsCards = getCrawledNewsCards(headLineLinks, categoryName, currentCategory)
            val persistenceNewsBundle = getRecentPersistenceNewsBundle(currentCategory, crawledDateTime)

            log.info { "crawledNewsCards size = ${crawledNewsCards.size}" }

            crawledNewsCards.map { crawledNewsCard ->
                val persistenceTargetNewsBundle = mutableListOf<News>()
                crawledNewsCard.map { crawledNews ->
                    val alreadySavedNews = isAlreadySavedNews(crawledNews, persistenceNewsBundle)
                    if (alreadySavedNews != null) {
                        alreadySavedNews.increaseCrawledCount()
                        persistenceTargetNewsBundle.add(alreadySavedNews)
                    } else {
                        persistenceTargetNewsBundle.add(crawledNews)
                    }
                }

                // 크롤러한 뉴스 삽입 전 마지막 News의 Index
                var currentLastNewsIndex = 1L

                // 현재 DB에 존재하는 가장 마지막 뉴스
                val lastNews = newsRepository.findTopByOrderByIdDesc()

                // 만약 DB에 뉴스가 존재한다면 해당 뉴스의 id + 1를 다음에 삽입될 인덱스로 지정
                if (lastNews != null) {
                    currentLastNewsIndex = lastNews.id + 1
                }

                // 크롤러한 뉴스 삽입 후 마지막 News의 Index
                val newNewsLastIndex = bulkInsertNews(persistenceTargetNewsBundle, crawledDateTime)
                val extractKeywordTargetNews = newsRepository.findById(newNewsLastIndex!!.toLong()).get()
                val extractedKeywords = getKeywords(extractKeywordTargetNews)

                log.info { "$extractedKeywords - keyword is extracted" }

                val persistenceNewsCard = NewsCard(
                    category = currentCategory,
                    multipleNews = filterSquareBracket(
                        (currentLastNewsIndex..newNewsLastIndex).joinToString(", ")
                    ),
                    keywords = extractedKeywords,
                    createdAt = LocalDateTime.now(),
                    modifiedAt = crawledDateTime,
                )
                persistenceTargetNewsCards.add(persistenceNewsCard)
                keywordsCountingPair += countKeyword(
                    keywordsCountingPair,
                    extractedKeywords,
                    currentCategory
                )
                if (isBulkInserTiming(persistenceTargetNewsCards)) {
                    bulkInsertNewsCard(persistenceTargetNewsCards, crawledDateTime)
                    persistenceTargetNewsCards.clear()
                }
            }
            log.info("$categoryName - crawled complete!!")
            Thread.sleep(1000)
        }
        if (isRemainedNewsCard(persistenceTargetNewsCards)) {
            bulkInsertNewsCard(persistenceTargetNewsCards, crawledDateTime)
        }

        rankingGenerator.saveKeywordRanking(keywordsCountingPair.mapValues { it.value.toInt() })

        log.info("$crawledDateTime - all crawling done")

        return crawledDateTime
    }

    private fun isBulkInserTiming(persistenceTargetNewsCards: MutableList<NewsCard>) =
        persistenceTargetNewsCards.size >= 100

    private fun bulkInsertNewsCard(
        persistenceTargetNewsCards: MutableList<NewsCard>,
        crawledDateTime: LocalDateTime,
    ) {
        newsCardBulkInsertRepository.bulkInsert(persistenceTargetNewsCards, crawledDateTime)
    }

    private fun bulkInsertNews(
        persistenceTargetNewsBundle: MutableList<News>,
        crawledDateTime: LocalDateTime,
    ) = newsBulkInsertRepository.bulkInsert(
        newsBundle = persistenceTargetNewsBundle,
        crawledDateTime = crawledDateTime
    )

    private fun getKeywords(extractKeywordTargetNews: News) = keywordExtractor.extractKeyword(
        title = extractKeywordTargetNews.title,
        content = extractKeywordTargetNews.content
    )

    private fun isRemainedNewsCard(persistenceTargetNewsCards: MutableList<NewsCard>) =
        persistenceTargetNewsCards.isNotEmpty()

    private fun getRecentPersistenceNewsBundle(
        currentCategory: Category,
        crawledDateTime: LocalDateTime,
    ) = newsRepository.findAllByCategoryAndCreatedAtBetween(
        category = currentCategory,
        startDateTime = crawledDateTime.minusDays(1),
        endDateTime = crawledDateTime
    )

    private fun getPersistenceCategory(categoryName: CategoryName) = when (categoryName) {
        POLITICS -> categoryRepository.findByName(POLITICS)
        ECONOMIC -> categoryRepository.findByName(ECONOMIC)
        SOCIETY -> categoryRepository.findByName(SOCIETY)
        CULTURE -> categoryRepository.findByName(CULTURE)
        WORLD -> categoryRepository.findByName(WORLD)
        SCIENCE -> categoryRepository.findByName(SCIENCE)
    }

    private fun getCrawledNewsCards(
        headLineLinks: Elements,
        categoryName: CategoryName,
        currentCategory: Category,
    ) = crawlerBase.extractNewsCardBundle(
        allHeadLineNewsLinks = crawlerBase.extractAllHeadLineNewsLinks(headLineLinks),
        categoryName = categoryName,
        category = currentCategory,
    )

    private fun getHeadLineLinks(
        categoryURL: String,
        categoryName: CategoryName,
    ) = crawlerBase.extractMoreHeadLineLinks(
        url = categoryURL,
        categoryName = categoryName
    )

    private fun countKeyword(
        keywordsCountingPair: MutableMap<String, Double>,
        extractedKeyword: String,
        category: Category,
    ): MutableMap<String, Double> {
        val keywords = extractedKeyword.split(", ")

        val weight = when (category.name) {
            POLITICS, SOCIETY -> CATEGORY_WEIGHT_ONE
            ECONOMIC -> CATEGORY_WEIGHT_ONE_QUARTER
            WORLD -> CATEGORY_WEIGHT_ONE_HALF
            CULTURE -> CATEGORY_WEIGHT_ONE_HALF_QUARTER
            SCIENCE -> CATEGORY_WEIGHT_TWO_HALF
        }

        keywords.map { keyword ->
            keywordsCountingPair[keyword] = keywordsCountingPair.getOrDefault(keyword, 0.0) + weight
        }

        return keywordsCountingPair
    }

    private fun filterSquareBracket(target: String): String {
        return target
            .replace("[", "")
            .replace("]", "")
    }

    private fun isAlreadySavedNews(crawledNews: News, persistenceNewsBundle: List<News>): News? {
        for (persistenceNews in persistenceNewsBundle) {
            if (crawledNews.title in persistenceNews.title &&
                crawledNews.newsLink in persistenceNews.newsLink &&
                crawledNews.press in persistenceNews.press
            ) {
                return persistenceNews
            }
        }
        return null
    }
}
