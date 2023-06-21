package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.NewsCard
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "member_news_card")
@Entity
class MemberNewsCard(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_card_id")
    val newsCard: NewsCard,
) : BaseEntity()
