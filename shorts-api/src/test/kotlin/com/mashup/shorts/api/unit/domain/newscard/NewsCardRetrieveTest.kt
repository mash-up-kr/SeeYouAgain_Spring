package com.mashup.shorts.api.unit.domain.newscard

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.repository.findByIdOrNull
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.member.MemberRepository
import com.mashup.shorts.domain.member.membercategory.MemberCategoryRepository
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newscard.NewsCard
import com.mashup.shorts.domain.newscard.NewsCardRepository
import com.mashup.shorts.domain.newscard.NewsCardRetrieve
import com.mashup.shorts.domain.newsnewscard.NewsNewsCardNativeQueryRepository
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension

@Disabled
@ExtendWith(MockKExtension::class)
class NewsCardRetrieveTest(

    @MockK
    private val memberRepository: MemberRepository,

    @MockK
    private val memberCategoryRepository: MemberCategoryRepository,

    @MockK
    private val newsCardRepository: NewsCardRepository,

    @MockK
    private val newsNewsCardNativeQueryRepository: NewsNewsCardNativeQueryRepository,
) {


    @Test
    @DisplayName("카드뉴스 내 뉴스 조회 - 첫 페이지 조회 시")
    fun loadDetailNewsInNewsCard() {
        // ready
        val newsCardRetrieve = NewsCardRetrieve(
            memberRepository,
            memberCategoryRepository,
            newsCardRepository,
            newsNewsCardNativeQueryRepository
        )

        val newsCardId = 1L
        val cursorId = 1L
        val size = 10
        val multipleNewsId = mutableListOf(1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L, 9L, 10L)

        // execute
        every { newsCardRepository.findByIdOrNull(newsCardId) } returns (
            NewsCard(
                category = Category(CategoryName.SCIENCE),
                multipleNews = multipleNewsId.toString().replace("[", "").replace("]", ""),
                keywords = "키워드1"
            ))
        every {
            newsNewsCardNativeQueryRepository.loadNewsBundleByCursorIdAndPersistenceNewsCardMultipleNews(
                cursorId = cursorId,
                persistenceNewsCardMultipleNews = multipleNewsId,
                size = size
            )
        } returns (
            mutableListOf(
                News(
                    title = "뉴스 제목",
                    content = "뉴스 내용",
                    thumbnailImageUrl = "이미지 링크",
                    newsLink = "뉴스 링크",
                    writtenDateTime = "작성시각",
                    type = "타입",
                    press = "언론사",
                    crawledCount = 1,
                    category = Category(CategoryName.SCIENCE)
                )
            ))
        val loadedNewsBundle = newsCardRetrieve.retrieveDetailNewsInNewsCard(newsCardId, cursorId, size)

        // validate
        assertThat(loadedNewsBundle.size).isEqualTo(1)
        assertThat(loadedNewsBundle).isInstanceOf(MutableList::class.java)
    }
}
