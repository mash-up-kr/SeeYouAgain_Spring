package com.mashup.shorts.domain.membernews

import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.NewsRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class MemberNewsDelete(
    private val newsRepository: NewsRepository,
    private val memberNewsRepository: MemberNewsRepository
) {

    fun deleteMemberNews(member: Member, newsIds: List<Long>) {
        val newsList = newsRepository.findAllById(newsIds)
        val memberNewsList = memberNewsRepository.findByNewsIn(newsList)
        if (memberNewsRepository.findByNewsIn(newsList).isEmpty()) {
            throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E404_NOT_FOUND,
                resultErrorMessage = "저장되지 않은 뉴스를 삭제할 수 없습니다."
            )
        }
        memberNewsList.map { it.softDelete() }
        // memberNewsRepository.deleteAllByMemberAndNewsIn(member, newsList)
    }
}
