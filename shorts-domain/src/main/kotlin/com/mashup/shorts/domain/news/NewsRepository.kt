package com.mashup.shorts.domain.news

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.category.Category

@Repository
interface NewsRepository : JpaRepository<News, Long>, NewsQueryDSLRepository {

    fun findAllByCategory(category: Category): List<News>
    fun findByTitleAndNewsLinkAndPress(title: String, newsLink: String, press: String): List<News>
    fun findTopByOrderByIdDesc(): News?
}
