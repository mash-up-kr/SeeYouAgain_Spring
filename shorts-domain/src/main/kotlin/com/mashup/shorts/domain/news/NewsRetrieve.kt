package com.mashup.shorts.domain.news

import java.time.LocalDateTime
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
        val startDateTime = targetDateTimePair.first
        val endDateTime = targetDateTimePair.second
        val newsBundle = newsRepository.findAllByCreatedAtBetween(
            startDateTime,
            endDateTime,
        ).filter { it.title.contains(keyword) }
        val newsIds = newsBundle.map { it.id }

        return newsRepository.loadNewsBundleByCursorIdAndTargetTime(
            startDateTime,
            endDateTime,
            cursorId,
            newsIds,
            size,
        )
    }

}
