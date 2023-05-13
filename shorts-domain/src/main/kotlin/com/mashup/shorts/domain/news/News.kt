package com.mashup.shorts.domain.news

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.news.card.NewsCard
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * News
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
@Table(name = "news")
@Entity
class News(
    @Column(name = "title", nullable = false)
    val title: String,

    @Column(name = "content", nullable = false)
    val content: String,

    @Column(name = "thumbnail_image_url", nullable = false)
    val thumbnailImageUrl: String,

    @Column(name = "news_link", nullable = false)
    val newsLink: String,

    @Column(name = "reporter", nullable = false)
    val reporter: String,

    @Column(name = "press", nullable = false)
    val press: String, // 언론사

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_card_id", nullable = false)
    val newsCard: NewsCard
): BaseEntity()