package com.mashup.shorts.core

import java.time.LocalDateTime
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.util.Slf4j2KotlinLogging.log
import com.mashup.shorts.core.const.NewsUrlConst.categoryToUrl
import com.mashup.shorts.core.keyword.KeywordExtractor
import com.mashup.shorts.core.util.CrawlerContentFilter
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

    @Scheduled(cron = "0 * * * * *")
    internal fun executeCrawling() {
        for (categoryPair in categoryToUrl) {
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

            val headLineLinks = crawlerBase.getMoreHeadLineLinks(
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

            for (newsCard in newsCardBundle) {
                val persistenceTargetNewsList = mutableListOf<News>()
                for (news in newsCard) {
                    if (news.title !in persistenceNewsBundle.map { it.title }) {
                        persistenceTargetNewsList.add(news)
                        newsRepository.save(news)
                    } else {
                        val alreadyExistNews = newsRepository.findByTitle(news.title) ?: throw ShortsBaseException.from(
                            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                            resultErrorMessage = "뉴스를 저장하는 중 ${news.title} 에 해당하는 뉴스를 찾을 수 없습니다."
                        )
                        alreadyExistNews.increaseCrawledCount()
                        newsRepository.save(alreadyExistNews)
                        persistenceTargetNewsList.add(alreadyExistNews)
                    }
                }

                // 뉴스 내용 가져올 인덱스를 0으로 고정할지 랜덤값을 넣을지 고려해봐야함.
                val extractKeyword = keywordExtractor.extractKeyword(persistenceTargetNewsList[0].content)
                val persistenceNewsCard = NewsCard(
                    category = category,
                    multipleNews = CrawlerContentFilter.filterSquareBracket(
                        persistenceTargetNewsList.map { it.id }.toString()
                    ),
                    keywords = extractKeyword
                )
                newsCardRepository.save(persistenceNewsCard)
            }
            log.info("Take a break for 1 seconds to prevent request overload")
            Thread.sleep(1000)
        }
        log.info(LocalDateTime.now().toString() + " - " + "crawling done")
    }


}
