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
) {

    @Scheduled(cron = "0 0 * * * *")
    internal fun executeCrawling() {
        val crawledDateTime = LocalDateTime.now()
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

                log.info("Take a break for 1 seconds to prevent request overload")
                Thread.sleep(1000)
            }
        }
        log.info("$crawledDateTime - crawling done")
    }

    private fun filterSquareBracket(target: String): String {
        return target
            .replace("[", "")
            .replace("]", "")
    }
}
