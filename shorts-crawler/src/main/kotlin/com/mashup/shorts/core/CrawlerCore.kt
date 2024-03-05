package com.mashup.shorts.core

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ofPattern
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Primary
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.core.consts.CATEGORY_WEIGHT_ONE
import com.mashup.shorts.core.consts.CATEGORY_WEIGHT_ONE_HALF
import com.mashup.shorts.core.consts.categoryToUrl
import com.mashup.shorts.core.keywordextractor.KeywordExtractor
import com.mashup.shorts.core.rank.RankingGenerator
import com.mashup.shorts.domain.category.Category
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

@Primary
@Component
class CrawlerCore(
    private val crawlerBase: CrawlerBase,
    private val categoryRepository: CategoryRepository,
    private val newsRepository: NewsRepository,
    private val newsBulkInsertRepository: NewsBulkInsertRepository,
    private val newsCardBulkInsertRepository: NewsCardBulkInsertRepository,
    @Qualifier("LuceneAnalyzerKeywordExtractor") private val keywordExtractor: KeywordExtractor,
    private val rankingGenerator: RankingGenerator,
) {

    @Retryable(value = [Exception::class], maxAttempts = 3)
    @Transactional(rollbackFor = [Exception::class])
    @Scheduled(cron = "0 0 * * * *")
    internal fun executeCrawling() {
        val crawledDateTime = LocalDateTime.now()
        val keywordsCountingPair = mutableMapOf<String, Double>()
        val persistenceTargetNewsCards = mutableListOf<NewsCard>()
        for (categoryPair in categoryToUrl) {
            val categoryName = categoryPair.key
            val categoryURL = categoryPair.value

            log.info { "$categoryName - ${crawledDateTime.format(ofPattern("yyyy-MM-dd HH:mm:ss"))} - crawling start" }

            val currentCategory = when (categoryName) {
                POLITICS -> categoryRepository.findByName(POLITICS)
                ECONOMIC -> categoryRepository.findByName(ECONOMIC)
                SOCIETY -> categoryRepository.findByName(SOCIETY)
                CULTURE -> categoryRepository.findByName(CULTURE)
                WORLD -> categoryRepository.findByName(WORLD)
                SCIENCE -> categoryRepository.findByName(SCIENCE)
            }

            val headLineLinks = crawlerBase.extractMoreHeadLineLinks(
                url = categoryURL,
                categoryName = categoryName
            )

            val crawledNewsCards = crawlerBase.extractNewsCardBundle(
                allHeadLineNewsLinks = crawlerBase.extractAllHeadLineNewsLinks(headLineLinks),
                categoryName = categoryName,
                category = currentCategory,
            )
            log.info { "crawledNewsCards size = ${crawledNewsCards.size}" }

            val persistenceNewsBundle = newsRepository.findAllByCategoryAndCreatedAtBetween(
                category = currentCategory,
                startDateTime = crawledDateTime.minusDays(1),
                endDateTime = crawledDateTime
            )

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
                val newNewsLastIndex = newsBulkInsertRepository.bulkInsert(
                    newsBundle = persistenceTargetNewsBundle,
                    crawledDateTime = crawledDateTime
                )

                val extractKeywordTargetNews = newsRepository.findById(newNewsLastIndex!!.toLong()).get()

                val extractedKeywords = keywordExtractor.extractKeyword(
                    title = extractKeywordTargetNews.title,
                    content = extractKeywordTargetNews.content
                )
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
                keywordsCountingPair += countKeyword(keywordsCountingPair, extractedKeywords, currentCategory)
                if (persistenceTargetNewsCards.size >= 100) {
                    newsCardBulkInsertRepository.bulkInsert(persistenceTargetNewsCards, crawledDateTime)
                    persistenceTargetNewsCards.clear()
                }
            }
            log.info("$categoryName - crawled complete!!")
            Thread.sleep(1000)
        }
        if (persistenceTargetNewsCards.isNotEmpty()) {
            newsCardBulkInsertRepository.bulkInsert(persistenceTargetNewsCards, crawledDateTime)
            persistenceTargetNewsCards.clear()
        }

        rankingGenerator.saveKeywordRanking(keywordsCountingPair.mapValues { it.value.toInt() })

        log.info("$crawledDateTime - all crawling done")
    }

    @Recover
    fun recover(exception: Exception) {
        log.error { "크롤링 중 예외가 발생하여 총 3회를 시도했으나 작업이 실패했습니다." }
        log.error { "ExceptionStackTrace : ${exception.localizedMessage}" }
        log.error { "ExceptionCause : ${exception.cause}" }
        throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E500_INTERNAL_SERVER_ERROR,
            resultErrorMessage = "크롤링 중 예외가 발생하여 총 3회를 시도했으나 작업이 실패했습니다."
        )
    }

    private fun countKeyword(
        keywordsCountingPair: MutableMap<String, Double>,
        extractedKeyword: String,
        category: Category,
    ): MutableMap<String, Double> {
        val keywords = extractedKeyword.split(", ")

        val weight = when (category.name) {
            POLITICS, ECONOMIC, SOCIETY -> CATEGORY_WEIGHT_ONE
            WORLD, CULTURE, SCIENCE -> CATEGORY_WEIGHT_ONE_HALF
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
