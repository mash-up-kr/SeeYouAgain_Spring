package com.mashup.shorts.domain.membernews

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.News
import jakarta.persistence.*

@Table(name = "member_news")
@Entity
class MemberNews(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    val member: Member,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    val news: News,

    @Column
    val savedFlag: SavedFlag
) : BaseEntity()
