package com.mashup.shorts.domain.newscard

import java.time.LocalDateTime
import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.category.Category
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Table(name = "news_card")
@Entity
class NewsCard(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    var category: Category,

    @Column(columnDefinition = "TEXT", name = "multiple_news", nullable = false, length = 5000)
    var multipleNews: String,

    @Column(name = "keywords", nullable = false, length = 100)
    var keywords: String,
) : BaseEntity() {
    constructor(
        category: Category,
        multipleNews: String,
        keywords: String,
        createdAt: LocalDateTime,
        modifiedAt: LocalDateTime,
    ) : this(category, multipleNews, keywords) {
        this.createdAt = createdAt
        this.modifiedAt = modifiedAt
    }
}
