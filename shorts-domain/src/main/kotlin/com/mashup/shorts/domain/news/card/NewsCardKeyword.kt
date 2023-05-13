package com.mashup.shorts.domain.news.card

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.keyword.Keyword
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * NewsCardKeyword
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
@Table(name = "news_card_keyword")
@Entity
class NewsCardKeyword(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id", nullable = false)
    val keyword: Keyword,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_card_id", nullable = false)
    val newsCard: NewsCard
): BaseEntity()
