package com.mashup.shorts.domain.news.newsnewscard

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
            "WHERE news.id > :cursorId and news.id in :newsCardMultipleNews " +
            "ORDER BY written_date_time ASC, news.id ASC " +
            "LIMIT :size",
        nativeQuery = true
    )
    fun loadNewsBundleByCursorAndNewsCardMultipleNewsASC(
        @Param("cursorId") cursorId: Long,
        @Param("newsCardMultipleNews") newsCardMultipleNews: List<Long>,
        @Param("size") size: Int,
    ): List<News>

    @Query(
        "SELECT * " +
            "FROM news " +
            "WHERE news.id > :cursorId and news.id in :newsCardMultipleNews " +
            "ORDER BY written_date_time DESC, news.id DESC " +
            "LIMIT :size",
        nativeQuery = true
    )
    fun loadNewsBundleByCursorAndNewsCardMultipleNewsDESC(
        @Param("cursorId") cursorId: Long,
        @Param("newsCardMultipleNews") newsCardMultipleNews: List<Long>,
        @Param("size") size: Int,
    ): List<News>
}
