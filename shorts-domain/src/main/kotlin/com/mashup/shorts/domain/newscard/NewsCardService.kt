package com.mashup.shorts.domain.newscard

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newsnewscard.NewsNewsCardNativeQueryRepository

@Service
@Transactional(readOnly = true)
class NewsCardService(
    private val newsCardRepository: NewsCardRepository,
    private val newsNewsCardNativeQueryRepository: NewsNewsCardNativeQueryRepository,
) {

    fun loadDetailNewsInNewsCard(
        newsCardId: Long,
        cursorId: Long,
        size: Int,
    ): MutableList<News> {
        val newsCard = newsCardRepository.findByIdOrNull(newsCardId)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "${newsCardId}에 해당 뉴스 카드는 존재하지 않습니다."
            )

        val newsIdBundle = newsCard.multipleNews.split(", ").map { it.toLong() }
        return newsNewsCardNativeQueryRepository.loadNewsBundleByCursorIdAndPersistenceNewsCardMultipleNews(
            cursorId,
            newsIdBundle,
            size
        )
    }
}
