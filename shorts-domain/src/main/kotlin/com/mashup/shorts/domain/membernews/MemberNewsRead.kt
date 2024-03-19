package com.mashup.shorts.domain.membernews

import java.time.LocalDateTime
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.memberlogbadge.MemberLogBadgeService
import com.mashup.shorts.domain.news.NewsRepository
import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode

@Service
@Transactional
class MemberNewsRead(
    private val newsRepository: NewsRepository,
    private val memberNewsRepository: MemberNewsRepository,
    private val memberLogBadgeService: MemberLogBadgeService,
) {

    fun clearNewsCard(member: Member, newsId: Long, now: LocalDateTime) {
        val news = newsRepository.findByIdOrNull(newsId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "뉴스를 읽음 처리하는 중 ${newsId}를 찾을 수 없습니다."
        )

        memberNewsRepository.findByMemberAndNews(member, news) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "저장하지 않은 뉴스를 읽음 처리 할 수 없습니다."
        )

        memberLogBadgeService.memberReadNewsLog(member)
    }
}
