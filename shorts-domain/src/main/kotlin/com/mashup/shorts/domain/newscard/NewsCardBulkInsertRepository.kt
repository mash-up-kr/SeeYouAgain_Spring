package com.mashup.shorts.domain.newscard

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository

@Repository
class NewsCardBulkInsertRepository(
    private val jdbcTemplate: JdbcTemplate,
) {


}
