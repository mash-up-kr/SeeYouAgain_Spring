package com.mashup.shorts.domain.newsnewscard

import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.news.News

@Repository
interface NewsNewsCardQueryDSLRepository {

    fun findNewsByCursorIdAndNewsCardId(
        cursorNewsId: Long,
        newsCardId: Long,
        pageSize: Int,
    ): MutableList<News>
}
