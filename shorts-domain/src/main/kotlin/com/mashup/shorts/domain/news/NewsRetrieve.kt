package com.mashup.shorts.domain.news

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND

@Service
@Transactional(readOnly = true)
class NewsRetrieve(
    private val newsRepository: NewsRepository,
) {
    fun retrieveNewsLinkByNewsId(newsId: Long): String {
        val news = newsRepository.findByIdOrNull(newsId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "뉴스 링크를 가져오는 중 요청한 NewsId : ${newsId}를 찾을 수 없습니다."
        )
        return news.newsLink
    }

}
