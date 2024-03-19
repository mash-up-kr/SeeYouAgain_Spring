package com.mashup.shorts.core.leagcy

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ofPattern
import org.springframework.retry.annotation.Recover
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.KeywordExtractor
import com.mashup.shorts.core.leagcy.consts.categoryToUrl
import com.mashup.shorts.domain.category.CategoryName.CULTURE
import com.mashup.shorts.domain.category.CategoryName.ECONOMIC
import com.mashup.shorts.domain.category.CategoryName.POLITICS
import com.mashup.shorts.domain.category.CategoryName.SCIENCE
import com.mashup.shorts.domain.category.CategoryName.SOCIETY
import com.mashup.shorts.domain.category.CategoryName.WORLD
import com.mashup.shorts.domain.category.CategoryRepository
import com.mashup.shorts.domain.keyword.HotKeyword
import com.mashup.shorts.domain.keyword.HotKeywordRepository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsBulkInsertRepository
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardBulkInsertRepository
import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode
import com.mashup.shorts.util.Slf4j2KotlinLogging.log

@Deprecated("Deprecated By Changed DOM")
@Component("DeprecatedCrawlerCore")
class CrawlerCore(
    private val crawlerBase: CrawlerBase,
    private val categoryRepository: CategoryRepository,
    private val newsRepository: NewsRepository,
    private val newsBulkInsertRepository: NewsBulkInsertRepository,
    private val newsCardBulkInsertRepository: NewsCardBulkInsertRepository,
    private val keywordExtractor: KeywordExtractor,
    private val hotKeywordRepository: HotKeywordRepository,
) {

    @Retryable(value = [Exception::class], maxAttempts = 3)
    @Transactional(rollbackFor = [Exception::class])
    internal fun executeCrawling() {
        val crawledDateTime = LocalDateTime.now()
        val keywordsCountingPair = mutableMapOf<String, Int>()
        val persistenceTargetNewsCards = mutableListOf<NewsCard>()
        for (categoryPair in categoryToUrl) {
            val categoryName = categoryPair.key
            val categoryURL = categoryPair.value

            log.info { "$categoryName - ${crawledDateTime.format(ofPattern("yyyy-MM-dd HH:mm:ss"))} - crawling start" }

            val category = when (categoryName) {
                POLITICS -> categoryRepository.findByName(POLITICS)
                ECONOMIC -> categoryRepository.findByName(ECONOMIC)
                SOCIETY -> categoryRepository.findByName(SOCIETY)
                CULTURE -> categoryRepository.findByName(CULTURE)
                WORLD -> categoryRepository.findByName(WORLD)
                SCIENCE -> categoryRepository.findByName(SCIENCE)
            }
            log.info { "${category.name.name} is loaded" }

            val headLineLinks = crawlerBase.extractMoreHeadLineLinks(
                url = categoryURL,
                categoryName = categoryName
            )

            val crawledNewsCards = crawlerBase.extractNewsCardBundle(
                allHeadLineNewsLinks = crawlerBase.extractAllHeadLineNewsLinks(headLineLinks),
                categoryName = categoryName,
                category = category,
            )
            log.info { "crawledNewsCards size = ${crawledNewsCards.size}" }


            val persistenceNewsBundle = newsRepository.findAllByCategoryAndCreatedAtBetween(
                category = category,
                startDateTime = crawledDateTime.minusDays(1),
                endDateTime = crawledDateTime
            )
            log.info { "persistenceNewsBundle size = ${persistenceNewsBundle.size}" }

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
                log.info { "$lastNews is loaded" }


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
                    category = category,
                    multipleNews = filterSquareBracket(
                        (currentLastNewsIndex..newNewsLastIndex).joinToString(", ")
                    ),
                    keywords = extractedKeywords,
                    createdAt = LocalDateTime.now(),
                    modifiedAt = crawledDateTime,
                )
                persistenceTargetNewsCards.add(persistenceNewsCard)
                keywordsCountingPair += countKeyword(keywordsCountingPair, extractedKeywords)
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
        saveKeywordRanking(keywordsCountingPair)
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


    //TODO: 테스트 코드 작성
    private fun saveKeywordRanking(keywordsCountingPair: Map<String, Int>) {
        //1위 ~ 10위까지 키워드 랭킹 산정 및 저장, value 기준 내림차순
        val sortedKeywords = keywordsCountingPair.toList().sortedByDescending { it.second }
        val keywordRanking = StringBuilder()

        val rankingSize = if (sortedKeywords.size < 10) sortedKeywords.size else 10
        for (rank: Int in 0 until rankingSize) {
            keywordRanking.append(sortedKeywords[rank]).append(", ")
        }

        hotKeywordRepository.save(HotKeyword(keywordRanking = keywordRanking.toString()))
    }

    //TODO: 테스트 코드 작성
    private fun countKeyword(
        keywordsCountingPair: MutableMap<String, Int>,
        extractedKeyword: String,
    ): MutableMap<String, Int> {
        val keywords = extractedKeyword.split(", ")

        for (keyword in keywords) {
            val count = keywordsCountingPair.getOrDefault(keyword, 0)
            keywordsCountingPair[keyword] = count + 1
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
