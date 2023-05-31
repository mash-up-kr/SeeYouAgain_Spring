package com.mashup.shorts.domain.news

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.category.Category
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "news")
@Entity
class News(
    @Column(name = "title", nullable = false, length = 200)
    val title: String,

    @Column(name = "content", nullable = false, length = 50000)
    val content: String,

    @Column(name = "thumbnail_image_url", nullable = false, length = 500)
    val thumbnailImageUrl: String,

    @Column(name = "news_link", nullable = false, length = 500)
    val newsLink: String,

    @Column(name = "press", nullable = false, length = 50)
    val press: String, // 언론사

    @Column(name = "written_date_time", nullable = false)
    val writtenDateTime: String,

    @Column(name = "type", nullable = false)
    val type: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,
) : BaseEntity()
