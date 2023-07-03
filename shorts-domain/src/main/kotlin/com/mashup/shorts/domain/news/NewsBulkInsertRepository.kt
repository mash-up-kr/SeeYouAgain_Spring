package com.mashup.shorts.domain.news

import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDateTime
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional


@Repository
class NewsBulkInsertRepository(
    private val jdbcTemplate: JdbcTemplate,
) {

    @Transactional
    fun bulkInsert(newsBundle: List<News>, crawledDateTime: LocalDateTime): Long? {
        val sql =
            """INSERT INTO news (title, content, news_link, press, thumbnail_image_url, type, written_date_time, crawled_count, category_id, created_at, modified_at)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """.trimMargin()

        jdbcTemplate.batchUpdate(
            sql,
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    val news = newsBundle[i]
                    ps.setString(1, news.title)
                    ps.setString(2, news.content)
                    ps.setString(3, news.newsLink)
                    ps.setString(4, news.press)
                    ps.setString(5, news.thumbnailImageUrl)
                    ps.setString(6, news.type)
                    ps.setString(7, news.writtenDateTime)
                    ps.setInt(8, news.crawledCount)
                    ps.setLong(9, news.category.id)
                    ps.setTimestamp(10, Timestamp.valueOf(crawledDateTime))
                    ps.setTimestamp(11, Timestamp.valueOf(LocalDateTime.now()))
                }

                override fun getBatchSize(): Int {
                    return newsBundle.size
                }
            }
        )
        return jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long::class.java)
    }
}
