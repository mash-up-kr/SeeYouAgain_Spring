package com.mashup.shorts.domain.membernews

import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.facade.memberlogbadge.MemberLogBadgeFacadeService
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.NewsRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
@Service
class MemberNewsCreate(
    private val newsRepository: NewsRepository,
    private val memberNewsRepository: MemberNewsRepository,
    private val memberLogBadgeFacadeService: MemberLogBadgeFacadeService,
) {
    @Transactional
    fun createMemberNews(member: Member, newsId: Long) {
        saveMemberNews(member, newsId)
        memberLogBadgeFacadeService.saveNewsLog(member = member)
    }

    private fun saveMemberNews(member: Member, newsId: Long) {
        val news = newsRepository.findByIdOrNull(newsId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${newsId}에 해당하는 뉴스가 존재하지 않습니다."
        )

        if (memberNewsRepository.existsByMemberAndNews(member, news)) {
            throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E400_BAD_REQUEST,
                resultErrorMessage = "뉴스는 중복해서 저장할 수 없습니다."
            )
        }
        memberNewsRepository.save(MemberNews(member = member, news = news))
    }
}
