package com.mashup.shorts.api.restdocs.newcard

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newscard.NewsCardController
import com.mashup.shorts.domain.newscard.NewsCardLoader
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(NewsCardController::class) // Presentation 영역만 테스트 한다는 의미
class NewsCardControllerRestDocsTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var newsCardLoader: NewsCardLoader //왜 lateinit var로 하는지?

    @Test
    fun 뉴스카드_내_뉴스조회() {
        every { newsCardLoader.loadDetailNewsInNewsCard(any(), any(), any()) } returns (
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

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/news-card/{newsCardId}", 1L)
                .param("cursorId", 0.toString())
                .param("size", 10.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "뉴스 카드 내 뉴스 조회",
                    RequestDocumentation.pathParameters(
                        RequestDocumentation
                            .parameterWithName("newsCardId")
                            .description("뉴스 카드 id"),
                    ),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("cursorId")
                            .description("커서 아이디"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("페이징 사이즈")
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("API 성공 여부"),
                        fieldWithPath("result[].id").type(JsonFieldType.NUMBER).description("뉴스 id"),
                        fieldWithPath("result[].title").type(JsonFieldType.STRING).description("뉴스 제목"),
                        fieldWithPath("result[].thumbnailImageUrl").type(JsonFieldType.STRING).description("뉴스 이미지 링크"),
                        fieldWithPath("result[].newsLink").type(JsonFieldType.STRING).description("뉴스 링크"),
                        fieldWithPath("result[].press").type(JsonFieldType.STRING).description("언론사"),
                        fieldWithPath("result[].writtenDateTime").type(JsonFieldType.STRING).description("작성 시각"),
                        fieldWithPath("result[].type").type(JsonFieldType.STRING).description("헤드라인 뉴스인지, 일반 뉴스인지"),
                    )
                )
            )
    }
}
