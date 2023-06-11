package com.mashup.shorts.api.unit.domain.newscard

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import com.mashup.shorts.domain.newscard.NewsCardRepository
import com.mashup.shorts.domain.newscard.NewsCardRetrieve
import com.mashup.shorts.domain.newsnewscard.NewsNewsCardNativeQueryRepository
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension

@ExtendWith(MockKExtension::class)
class NewsCardRetrieveTest(

    @MockK
    private val newsCardRepository: NewsCardRepository,

    @MockK
    private val newsNewsCardNativeQueryRepository: NewsNewsCardNativeQueryRepository,

    @InjectMockKs
    private val newsCardRetrieve: NewsCardRetrieve,
) {


    @Test
    @DisplayName("카드뉴스 내 뉴스 조회 - 첫 페이지 조회 시")
    fun loadDetailNewsInNewsCard() {
        // ready
        val newsCardId = 1L
        val cursorId = 1L
        val size = 10

        // execute
        val loadedNewsBundle = newsCardRetrieve.loadDetailNewsInNewsCard(newsCardId, cursorId, size)

        // validate
        assertThat(loadedNewsBundle.size).isEqualTo(10)
        assertThat(loadedNewsBundle).isInstanceOf(MutableList::class.java)
    }
}
