package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.NewsCard
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MemberNewsCardRepository : JpaRepository<MemberNewsCard, Long>, MemberNewsCardQueryDSLRepository {

    fun findAllByMember(member: Member): List<MemberNewsCard>
    fun findByNewsCardIn(newsCard: List<NewsCard>): List<MemberNewsCard>
    fun existsByMemberAndNewsCard(member: Member, newsCard: NewsCard): Boolean
    fun deleteAllByMemberAndNewsCardIn(member: Member, newsList: List<NewsCard>)
    fun deleteByNewsCard(newsCard: NewsCard)
    fun countAllByMember(member: Member): Int
}
