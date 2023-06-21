package com.mashup.shorts.domain.member.membernews

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.NewsRepository

@Service
@Transactional
class MemberNewsDelete(
    private val newsRepository: NewsRepository,
    private val memberNewsRepository: MemberNewsRepository
) {

    fun deleteMemberNews(member: Member, newsIds: List<Long>) {
        val newsList = newsRepository.findAllById(newsIds)
        memberNewsRepository.deleteAllByMemberAndNewsIn(member, newsList)
    }
}
