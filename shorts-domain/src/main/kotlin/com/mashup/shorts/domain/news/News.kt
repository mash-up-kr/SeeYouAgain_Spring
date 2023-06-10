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

    @Column(columnDefinition = "TEXT", name = "title", nullable = false)
    val title: String,

    @Column(columnDefinition = "TEXT", name = "content", nullable = false)
    val content: String,

    @Column(columnDefinition = "TEXT", name = "thumbnail_image_url", nullable = false, length = 255)
    val thumbnailImageUrl: String,

    @Column(columnDefinition = "TEXT", name = "news_link", nullable = false, length = 500)
    val newsLink: String,

    @Column(name = "press", nullable = false, length = 20)
    val press: String, // 언론사

    @Column(name = "written_date_time", nullable = false, length = 30)
    val writtenDateTime: String,

    @Column(name = "type", nullable = false, length = 10)
    val type: String,

    @Column(name = "crawled_count", nullable = false)
    var crawledCount: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    val category: Category,
) : BaseEntity() {

    fun increaseCrawledCount() {
        this.crawledCount += 1
    }
}
