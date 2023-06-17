package com.mashup.shorts.domain.newscard

import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.newsnewscard.NewsNewsCardNativeQueryRepository
import com.mashup.shorts.domain.newscard.Pivots.ASC
import com.mashup.shorts.domain.newscard.Pivots.DESC
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class NewsCardRetrieve(
    private val newsCardRepository: NewsCardRepository,
    private val newsNewsCardNativeQueryRepository: NewsNewsCardNativeQueryRepository,
) {

    fun retrieveDetailNewsInNewsCard(
        newsCardId: Long,
        cursorId: Long,
        size: Int,
        pivot: String,
    ): List<News> {
        if (pivot != ASC.name && pivot != DESC.name) {
            throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E400_BAD_REQUEST,
                resultErrorMessage = "잘못된 정렬 기준인 ${pivot}를 요청했습니다."
            )
        }
        val newsCard = newsCardRepository.findByIdOrNull(newsCardId)
            ?: throw ShortsBaseException.from(
                shortsErrorCode = E404_NOT_FOUND,
                resultErrorMessage = "${newsCardId}에 해당 뉴스 카드는 존재하지 않습니다."
            )
        val newsIdBundle = newsCard.multipleNews.split(", ").map { it.toLong() }

        if (pivot == ASC.name) {
            return newsNewsCardNativeQueryRepository.loadNewsBundleByCursorAndNewsCardMultipleNewsASC(
                cursorId,
                newsIdBundle,
                size
            )
        }

        return newsNewsCardNativeQueryRepository.loadNewsBundleByCursorAndNewsCardMultipleNewsDESC(
            cursorId,
            newsIdBundle,
            size
        )
    }
}
