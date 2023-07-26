package com.mashup.shorts.domain.membernews

import java.time.LocalDateTime
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.News

@Repository
interface MemberNewsRepository : JpaRepository<MemberNews, Long>, MemberNewsQueryDSLRepository {

    fun deleteAllByMemberAndNewsIn(member: Member, newsList: List<News>)

    fun existsByMemberAndNews(member: Member, news: News): Boolean
    fun findByNewsIn(news: List<News>): List<MemberNews>

    fun countAllByMember(member: Member): Int

    fun findAllByMember(member: Member): List<MemberNews>

    fun countByMemberAndCreatedAtBetween(
        member: Member,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime,
    ): Int
}
