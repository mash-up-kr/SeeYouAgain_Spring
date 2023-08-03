package com.mashup.shorts.domain.membernews

import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.facade.memberlogbadge.MemberLogBadgeFacadeService
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.NewsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberNewsClear(
    private val newsRepository: NewsRepository,
    private val memberNewsRepository: MemberNewsRepository,
    private val memberLogBadgeFacadeService: MemberLogBadgeFacadeService
) {

    fun clearNews(member: Member, newsId: Long) {
        val news = newsRepository.findByIdOrNull(newsId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "뉴스를 다 읽음 처리하는 중 ${newsId}를 찾을 수 없습니다."
        )

        memberNewsRepository.findByMemberAndNews(member, news) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "저장하지 않은 뉴스를 읽음 처리 할 수 없습니다."
        )
        memberLogBadgeFacadeService.memberClearingNewsLog(member, news)
    }
}