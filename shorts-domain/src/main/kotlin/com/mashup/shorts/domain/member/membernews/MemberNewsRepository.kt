package com.mashup.shorts.domain.member.membernews

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.News

@Repository
interface MemberNewsRepository : JpaRepository<MemberNews, Long> {

    fun existsByMemberAndNews(member: Member, news: News): Boolean
}
