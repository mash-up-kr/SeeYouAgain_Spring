package com.mashup.shorts.core

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.core.const.categoryToUrl
import com.mashup.shorts.core.keyword.KeywordExtractor
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
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository

@Component
@Transactional
class CrawlerCore(
    private val newsRepository: NewsRepository,
    private val newsCardRepository: NewsCardRepository,
    private val categoryRepository: CategoryRepository,
    private val crawlerBase: CrawlerBase,
    private val keywordExtractor: KeywordExtractor,
    private val hotKeywordRepository: HotKeywordRepository,
) {

    @Scheduled(cron = "0 0 * * * *")
    internal fun executeCrawling() {
        val crawledDateTime = LocalDateTime.now()
        var keywordsCountingPair = mutableMapOf<String, Int>()
        for (categoryPair in categoryToUrl) {
            log.info {
                "${categoryPair.key} - ${crawledDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))} - crawling start"
            }
            val category = when (categoryPair.key) {
                POLITICS -> categoryRepository.findByName(POLITICS)
                ECONOMIC -> categoryRepository.findByName(ECONOMIC)
                SOCIETY -> categoryRepository.findByName(SOCIETY)
                CULTURE -> categoryRepository.findByName(CULTURE)
                WORLD -> categoryRepository.findByName(WORLD)
                SCIENCE -> categoryRepository.findByName(SCIENCE)
                else -> throw ShortsBaseException.from(
                    shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                    resultErrorMessage = "${LocalDateTime.now()} - 크롤링 시도 중 ${categoryPair.key} 를 찾을 수 없습니다."
                )
            }

            val headLineLinks = crawlerBase.extractMoreHeadLineLinks(
                url = categoryPair.value,
                categoryName = categoryPair.key
            )
            val allNewsLinks = crawlerBase.extractAllHeadLineNewsLinks(headLineLinks)
            val persistenceNewsBundle = newsRepository.findAllByCategory(category)
            val newsCardBundle = crawlerBase.extractNewsCardBundle(
                allNewsLinks,
                categoryPair.key,
                category,
            )

            newsCardBundle.map { newsCard ->
                val persistenceTargetNewsBundle = mutableListOf<News>()
                newsCard.map { crawledNews ->
                    // 이미 저장한 뉴스인 경우
                    if (isAlreadySavedNews(
                            news = crawledNews,
                            persistenceNewsBundle = persistenceNewsBundle,
                        )
                    ) {
                        val alreadyExistNews = newsRepository.findByTitleAndContent(
                            title = crawledNews.title,
                            content = crawledNews.content,
                        ).first()
                        alreadyExistNews.increaseCrawledCount()
                        persistenceTargetNewsBundle.add(alreadyExistNews)
                    }
                    // DB에 존재하지 않는 뉴스인 경우
                    else {
                        persistenceTargetNewsBundle.add(crawledNews)
                        newsRepository.save(crawledNews)
                    }
                }

                val extractedKeywords = keywordExtractor.extractKeywordV2(
                    persistenceTargetNewsBundle.first().content
                )
                val persistenceNewsCard = NewsCard(
                    category = category,
                    multipleNews = filterSquareBracket(
                        persistenceTargetNewsBundle.map { it.id }.toString()
                    ),
                    keywords = extractedKeywords,
                    createdAt = crawledDateTime,
                    modifiedAt = crawledDateTime,
                )

                newsCardRepository.save(persistenceNewsCard)

                keywordsCountingPair += countKeyword(keywordsCountingPair, extractedKeywords)
            }
            log.info("${categoryPair.key} - crawled complete!!")
            Thread.sleep(1000)
        }
        log.info("$crawledDateTime - all crawling done")
        saveKeywordRanking(keywordsCountingPair)
    }

    //TODO: 테스트 코드 작성
    private fun saveKeywordRanking(keywordsCountingPair: Map<String, Int>) {
        //1위 ~ 10위까지 키워드 랭킹 산정 및 저장, value 기준 내림차순
        val sortedKeywords = keywordsCountingPair.toList().sortedByDescending { it.second }
        val keywordRanking = StringBuilder()

        for (rank: Int in 0..9) {
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

    private fun isAlreadySavedNews(news: News, persistenceNewsBundle: List<News>): Boolean {
        return news.title in persistenceNewsBundle.map { it.title } &&
            news.content in persistenceNewsBundle.map { it.content }
    }
}
