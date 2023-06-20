package com.mashup.shorts.domain.news

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.category.Category

@Repository
interface NewsRepository : JpaRepository<News, Long> {

    fun findAllByCategory(category: Category): List<News>

    fun findByTitle(title: String): News?

    @Query("SELECT distinct * FROM news WHERE news.title = :title", nativeQuery = true)
    fun customFindByTitle(title: String): News?


}
