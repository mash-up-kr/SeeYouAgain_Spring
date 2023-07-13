package com.mashup.shorts.domain.news

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.common.util.StartEndDateTimeExtractor.extractStarDateTimeAndEndDateTime
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.MemberNewsRepository

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

    fun retrieveByCompany(
        companies: List<String>,
        cursorId: Long,
        size: Int,
    ): List<News> {
        val startDateTime = LocalDateTime.of(
            LocalDate.now(),
            LocalTime.of(LocalTime.now().hour, LocalTime.now().minute, 0, 0)
        ).minusDays(7)
        val endDateTime = LocalDateTime.of(
            LocalDate.now(),
            LocalTime.of(LocalTime.now().hour, LocalTime.now().minute, 59, 999999999)
        )

        return newsRepository.loadNewsBundleByCursorAndCompany(
            companies = companies,
            startDateTime = startDateTime,
            endDateTime = endDateTime,
            cursorId = cursorId,
            size = size
        )
    }

}
