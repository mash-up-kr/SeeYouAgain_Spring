package com.mashup.shorts.domain.membernewscard

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newscard.NewsCard

@Repository
interface MemberNewsCardRepository : JpaRepository<MemberNewsCard, Long> {

    fun findAllByMember(member: Member): List<MemberNewsCard>
    fun existsByMemberAndNewsCard(member: Member, newsCard: NewsCard): Boolean
    fun deleteAllByMemberAndNewsCardIn(member: Member, newsList: List<NewsCard>)
    fun deleteByMember(member: Member)
    fun countAllByMember(member: Member): Int
}
