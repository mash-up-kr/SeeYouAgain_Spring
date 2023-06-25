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
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.category.CategoryName.*
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
    private val hotKeywordRepository: HotKeywordRepository
) {

    @Scheduled(cron = "0 0 * * * *")
    internal fun executeCrawling() {
        val crawledDateTime = LocalDateTime.now()
        val numOfKeywords = mutableMapOf<String, Int>()
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
                val persistenceTargetNewsList = mutableListOf<News>()
                newsCard.map { news ->
                    if (news.title !in persistenceNewsBundle.map { it.title }) {
                        persistenceTargetNewsList.add(news)
                        newsRepository.save(news)
                    } else {
                        val alreadyExistNews =
                            newsRepository.findByTitleAndPress(news.title, news.press) ?: throw ShortsBaseException.from(
                                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                                resultErrorMessage = "뉴스를 저장하는 중 ${news.title} 에 해당하는 뉴스를 찾을 수 없습니다."
                            )
                        alreadyExistNews.increaseCrawledCount()
                        newsRepository.save(alreadyExistNews)
                        persistenceTargetNewsList.add(alreadyExistNews)
                    }
                }

                val extractKeyword = keywordExtractor.extractKeyword(
                    persistenceTargetNewsList[0].content
                )
                val persistenceNewsCard = NewsCard(
                    category = category,
                    multipleNews = filterSquareBracket(
                        persistenceTargetNewsList.map { it.id }.toString()
                    ),
                    keywords = extractKeyword,
                    createdAt = crawledDateTime,
                    modifiedAt = crawledDateTime,
                )
                newsCardRepository.save(persistenceNewsCard)

                //TODO: 키워드 횟수 카운트
                countKeyword(numOfKeywords, extractKeyword)

                log.info("Take a break for 1 seconds to prevent request overload")
                Thread.sleep(1000)
            }
        }
        log.info("$crawledDateTime - crawling done")

        //TODO: 키워드 랭킹 저장
        saveKeywordRanking(numOfKeywords)
    }

    //TODO: 테스트 코드 작성
    private fun saveKeywordRanking(numOfKeywords: Map<String, Int>) {
        //1위 ~ 10위까지 키워드 랭킹 산정 및 저장, value 기준 내림차순
        val sortedKeywords = numOfKeywords.toList().sortedByDescending { it.second }
        val keywordRanking = StringBuilder()
        for (rank: Int in 0..9) {
            keywordRanking.append(sortedKeywords[rank]).append(", ")
        }
        hotKeywordRepository.save(HotKeyword(keywordRanking = keywordRanking.toString()))
    }

    //TODO: 테스트 코드 작성
    private fun countKeyword(numOfKeywords: Map<String, Int>, extractKeyword: String) {
        val keywords = extractKeyword.split(",")
        for (keyword: String in keywords) {
            val cnt = numOfKeywords.getOrDefault(keyword, 0)
            numOfKeywords.plus(Pair(keyword, cnt + 1))
        }
    }

    private fun filterSquareBracket(target: String): String {
        return target
            .replace("[", "")
            .replace("]", "")
    }
}
