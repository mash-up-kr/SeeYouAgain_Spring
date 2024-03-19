package com.mashup.shorts.domain.news

import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.util.StartEndDateTimeExtractor.extractStarDateTimeAndEndDateTime
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.MemberNewsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
@Transactional(readOnly = true)
class NewsRetrieve(
    private val memberNewsRepository: MemberNewsRepository,
    private val newsRepository: NewsRepository,
) {

    fun retrieveNews(member: Member, newsId: Long): NewsRetrieveInfo {
        val news = newsRepository.findByIdOrNull(newsId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "뉴스 링크를 가져오는 중 요청한 NewsId : ${newsId}를 찾을 수 없습니다."
        )
        if (memberNewsRepository.existsByMemberAndNews(member, news)) {
            return NewsRetrieveInfo(newsLink = news.newsLink, isSaved = true)
        }
        return NewsRetrieveInfo(newsLink = news.newsLink, isSaved = false)
    }

    fun retrieveByKeyword(
        targetDateTime: LocalDateTime,
        keyword: String,
        cursorId: Long,
        size: Int,
    ): List<News> {
        val targetDateTimePair = extractStarDateTimeAndEndDateTime(targetDateTime)
        var startDateTime = targetDateTimePair.first
        var endDateTime = targetDateTimePair.second

        var newsBundle = newsRepository.loadNewsBundleByCursorAndKeyword(
            keyword = keyword,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            cursorId = cursorId,
            size = size
        )

        while (newsBundle.isEmpty()) {
            startDateTime = startDateTime.minusHours(1)
            endDateTime = endDateTime.minusHours(1)
            newsBundle = newsRepository.loadNewsBundleByCursorAndKeyword(
                keyword = keyword,
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                cursorId = cursorId,
                size = size
            )
        }
        return newsBundle
    }

}
