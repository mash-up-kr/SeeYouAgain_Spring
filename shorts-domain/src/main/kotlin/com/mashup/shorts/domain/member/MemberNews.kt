package com.mashup.shorts.domain.member

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.news.News
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "member_news")
@Entity
class MemberNews(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    val news: News,
) : BaseEntity()
