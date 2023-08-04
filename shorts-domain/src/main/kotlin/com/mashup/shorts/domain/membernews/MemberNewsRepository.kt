package com.mashup.shorts.domain.membernews

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberNewsRepository : JpaRepository<MemberNews, Long> {

    fun deleteAllByMemberAndNewsIn(member: Member, newsList: List<News>)

    fun existsByMemberAndNews(member: Member, news: News): Boolean
    fun findByNewsIn(news: List<News>): List<MemberNews>

    fun countAllByMember(member: Member): Int

    fun findAllByMember(member: Member): List<MemberNews>

    fun findByMemberAndNews(member: Member, news: News): MemberNews?
}
