package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.NewsCard
import jakarta.persistence.*

@Table(name = "member_news")
@Entity
class MemberNewsCard(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_card_id")
    val newsCard: NewsCard,
) : BaseEntity()