package com.mashup.shorts.domain.member.membernews

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode.E404_NOT_FOUND
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.news.NewsRepository

@Service
@Transactional
class MemberNewsDelete(
    private val memberRepository: MemberRepository,
    private val newsRepository: NewsRepository,
    private val memberNewsRepository: MemberNewsRepository
) {

    fun deleteMemberNews(memberUniqueId: String, newsIds: List<Long>) {
        val member = memberRepository.findByUniqueId(memberUniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = E404_NOT_FOUND,
            resultErrorMessage = "${memberUniqueId}에 해당하는 유저가 존재하지 않습니다."
        )
        val newsList = newsRepository.findAllById(newsIds)
        memberNewsRepository.deleteAllByMemberAndNewsIn(member, newsList)
    }
}
