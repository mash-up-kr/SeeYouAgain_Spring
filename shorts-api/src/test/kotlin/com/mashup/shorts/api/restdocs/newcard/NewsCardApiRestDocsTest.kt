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
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.newscard.NewsCardApi
import com.mashup.shorts.domain.newscard.NewsCardRetrieve
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(NewsCardApi::class)
class NewsCardApiRestDocsTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var newsCardRetrieve: NewsCardRetrieve

    @Test
    fun 뉴스카드_내_뉴스조회() {
        val cursorWrittenDateTime = "2023.06.15. 오후 3:38"
        val size = 10
        val pivot = "ASC"
        every { newsCardRetrieve.retrieveDetailNewsInNewsCard(any(), any(), any(), any()) } returns (
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
                .param("cursorWrittenDateTime", cursorWrittenDateTime)
                .param("size", size.toString())
                .param("pivot", pivot)
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "뉴스 카드 내 뉴스 조회",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation
                            .parameterWithName("newsCardId")
                            .description("조회할 뉴스 카드의 Id"),
                    ),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("cursorWrittenDateTime")
                            .description("커서 지정 값 ex) 2023.06.15. 오후 3:38 와 같이 입력해 주시고, 첫 페이지 요청 시 빈 문자열을 넣어주세요"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 10까지 허용합니다.)"),
                        RequestDocumentation
                            .parameterWithName("pivot")
                            .description("<필수값> 정렬 기준 [ASC, DESC] 둘 중 하나만 허용합니다. ASC는 오래된 순, DESC는 최신 순 입니다."),
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
