package com.mashup.shorts.domain.member.membernews

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.news.NewsRepository

@Transactional(readOnly = true)
@Service
class MemberNewsCreate(
    private val memberRepository: MemberRepository,
    private val newsRepository: NewsRepository,
    private val memberNewsRepository: MemberNewsRepository
) {

    @Transactional
    fun createMemberNews(uniqueId: String, newsId: Long) {
        val member = memberRepository.findByUniqueId(uniqueId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${uniqueId}에 해당하는 유저가 존재하지 않습니다."
        )
        val news = newsRepository.findByIdOrNull(newsId) ?: throw ShortsBaseException.from(
            shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
            resultErrorMessage = "${newsId}에 해당하는 뉴스가 존재하지 않습니다."
        )
        memberNewsRepository.save(MemberNews(member = member, news = news))
    }
}
