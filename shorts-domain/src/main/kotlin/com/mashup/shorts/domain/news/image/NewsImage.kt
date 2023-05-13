package com.mashup.shorts.domain.news.image

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.news.News
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

/**
 * NewsImage
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
@Table(name = "news_image")
@Entity
class NewsImage(
    @Column(name = "image_url")
    val imageUrl: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    val news: News
): BaseEntity()
