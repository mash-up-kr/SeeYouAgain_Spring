package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.NewsCard
import org.springframework.data.jpa.repository.JpaRepository

interface MemberNewsCardRepository : JpaRepository<MemberNewsCard, Long> {

    fun findAllByMember(member: Member): List<MemberNewsCard>

    fun existsByMemberAndNewsCard(member: Member, newsCard: NewsCard): Boolean

    fun findByNewsCardIn(newsCard: List<NewsCard>): List<MemberNewsCard>
    fun deleteAllByMemberAndNewsCardIn(member: Member, newsList: List<NewsCard>)
}