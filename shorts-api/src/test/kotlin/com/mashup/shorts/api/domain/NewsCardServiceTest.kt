package com.mashup.shorts.api.domain

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import com.mashup.shorts.domain.newscard.NewsCardService
import com.mashup.shorts.domain.newsnewscard.NewsNewsCardQueryDSLRepository

@SpringBootTest
class NewsCardServiceTest(
    @Autowired
    private val newsCardService: NewsCardService,

    @Autowired
    private val newsNewsCardQueryDSLRepository: NewsNewsCardQueryDSLRepository,
) {

    @Test
    @DisplayName("카드뉴스 내 뉴스 조회")
    fun loadDetailNewsInNewsCard() {
        val page1 = newsCardService.loadDetailNewsInNewsCard(1, 1, 10)
        val page2 = newsCardService.loadDetailNewsInNewsCard(1, 10, 10)
        val page3 = newsCardService.loadDetailNewsInNewsCard(1, 5, 5)

        println("page1.size = ${page1.size}")
        println("page2.size = ${page2.size}")
        println("page3.size = ${page3.size}")

        for (news in page1) {
            println("news.id = ${news.id}")
        }

        for (news in page2) {
            println("news.id = ${news.id}")
        }
    }

    @Test
    @DisplayName("카드뉴스 내 뉴스 조회")
    fun loadDetailNewsInNewsCard2() {
        val findNewsByCursorIdAndNewsCardId = newsNewsCardQueryDSLRepository.findNewsByCursorIdAndNewsCardId(
            1L,
            1L,
            10
        )

        for (news in findNewsByCursorIdAndNewsCardId) {
            println("news.title = ${news.title}")
        }
    }
}
