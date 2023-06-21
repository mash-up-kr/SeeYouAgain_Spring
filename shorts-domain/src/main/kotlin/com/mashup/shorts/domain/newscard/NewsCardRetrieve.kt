package com.mashup.shorts.domain.newscard

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newsnewscard.NewsNewsCardQueryDSLRepository

@Service
@Transactional(readOnly = true)
class NewsCardRetrieve(
    private val newsCardRepository: NewsCardRepository,
    private val newsNewsCardQueryDSLRepository: NewsNewsCardQueryDSLRepository,
) {

    fun retrieveDetailNewsInNewsCard(
        newsCardId: Long,
        cursorWrittenDateTime: String,
        size: Int,
        pivot: Pivots,
    ): List<News> {
        val newsCard = newsCardRepository.findByIdOrNull(newsCardId)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = E404_NOT_FOUND,
                resultErrorMessage = "${newsCardId}에 해당 뉴스 카드는 존재하지 않습니다."
            )
        val newsIdBundle = newsCard.multipleNews.split(", ").map { it.toLong() }

        return newsNewsCardQueryDSLRepository.loadNewsBundleByCursorAndNewsCardMultipleNews(
            cursorWrittenDateTime,
            newsIdBundle,
            size,
            pivot
        )
    }
}
