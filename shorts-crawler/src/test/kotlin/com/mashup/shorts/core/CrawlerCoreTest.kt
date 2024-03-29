package com.mashup.shorts.core

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import com.mashup.shorts.ShortsCrawlerApplication
import com.mashup.shorts.core.modern.CrawlerCore

@Disabled
@ActiveProfiles("test")
@SpringBootTest(classes = [ShortsCrawlerApplication::class])
class CrawlerCoreTest (
    @Autowired private val crawlerCore: CrawlerCore,
) {

    @Test
    @DisplayName("모든 카테고리 한 번에 크롤링 해오기")
    fun execute() {
        crawlerCore.execute()
    }
}
