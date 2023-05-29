package com.mashup.shorts.domain.newscard

import com.mashup.shorts.domain.BaseEntity
import com.mashup.shorts.domain.category.Category
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
    val category: Category,
    val multipleNews: String,
    val keywords: String,
) : BaseEntity()
