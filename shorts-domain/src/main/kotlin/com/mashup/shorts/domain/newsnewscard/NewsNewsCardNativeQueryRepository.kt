package com.mashup.shorts.domain.newsnewscard

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.news.News

@Repository
interface NewsNewsCardNativeQueryRepository : JpaRepository<News, Long> {

    @Query(
        "SELECT * " +
            "FROM news " +
            "WHERE news.id > :cursorId and news.id in :persistenceNewsCardMultipleNews " +
            "ORDER BY news.id asc " +
            "LIMIT :size",
        nativeQuery = true
    )
    fun loadNewsBundleByCursorIdAndPersistenceNewsCardMultipleNews(
        @Param("cursorId") cursorId: Long,
        @Param("persistenceNewsCardMultipleNews") persistenceNewsCardMultipleNews: List<Long>,
        @Param("size") size: Int,
    ): MutableList<News>
}
