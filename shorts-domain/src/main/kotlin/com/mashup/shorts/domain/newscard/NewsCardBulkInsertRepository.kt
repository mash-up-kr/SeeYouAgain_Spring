package com.mashup.shorts.domain.newscard

import java.sql.PreparedStatement
import java.sql.Timestamp
import java.time.LocalDateTime
import org.springframework.jdbc.core.BatchPreparedStatementSetter
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class NewsCardBulkInsertRepository(
    private val jdbcTemplate: JdbcTemplate,
) {

    @Transactional
    fun bulkInsert(newsCards: List<NewsCard>, crawledDateTime: LocalDateTime) {
        val sql =
            """INSERT INTO news_card (multiple_news, keywords, category_id, created_at, modified_at)
                VALUES (?, ?, ?, ?, ?)
            """.trimMargin()

        jdbcTemplate.batchUpdate(
            sql,
            object : BatchPreparedStatementSetter {
                override fun setValues(ps: PreparedStatement, i: Int) {
                    val newsCard = newsCards[i]
                    ps.setString(1, newsCard.multipleNews)
                    ps.setString(2, newsCard.keywords)
                    ps.setLong(3, newsCard.category.id)
                    ps.setTimestamp(4, Timestamp.valueOf(newsCard.createdAt))
                    ps.setTimestamp(5, Timestamp.valueOf(newsCard.modifiedAt))
                }

                override fun getBatchSize(): Int {
                    return newsCards.size
                }
            }
        )
    }
}
