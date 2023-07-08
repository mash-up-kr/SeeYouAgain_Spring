package com.mashup.shorts.api.restdocs.news

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.news.NewsRetrieve
import com.mashup.shorts.domain.home.news.NewsRetrieveApi
import com.mashup.shorts.domain.news.News
import com.mashup.shorts.domain.news.NewsRetrieveInfo
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(NewsRetrieveApi::class)
class NewsRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var newsRetrieve: NewsRetrieve

    @Test
    fun `뉴스 상세 조회`() {
        every { newsRetrieve.retrieveNews(any(), any()) } returns (
            NewsRetrieveInfo(newsLink = "https://shorts.com", isSaved = true)
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/news/{newsId}", 1)
                .header("Authorization", "Bearer test-user")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "뉴스 상세 조회",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    RequestDocumentation.pathParameters(
                        RequestDocumentation.parameterWithName("newsId").description("조회할 뉴스 id")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.newsLink").type(JsonFieldType.STRING).description("뉴스 링크"),
                        PayloadDocumentation.fieldWithPath("result.isSaved").type(JsonFieldType.BOOLEAN).description("뉴스 저장 여부")
                    )
                )
            )
    }

    @Test
    fun `핫 키워드로 뉴스 조회`() {
        every {
            newsRetrieve.retrieveByKeyword(
                any(), any(), any(), any()
            )
        } returns (
            listOf(
                News(
                    title = "Title~!",
                    content = "Contents ",
                    thumbnailImageUrl = "IMAGE LINK",
                    newsLink = "NEWS LINK",
                    press = "TYN",
                    writtenDateTime = "2023.06.29. 오전 11:41",
                    type = "HEADLINE",
                    crawledCount = 1,
                    category = Category(CategoryName.CULTURE)
                ),
                News(
                    title = "Title~!",
                    content = "Contents ",
                    thumbnailImageUrl = "IMAGE LINK",
                    newsLink = "NEWS LINK",
                    press = "TYN",
                    writtenDateTime = "2023.06.29. 오전 11:41",
                    type = "HEADLINE",
                    crawledCount = 1,
                    category = Category(CategoryName.CULTURE)
                ),
            )
            )

        mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/news")
                .param("keyword", "대통령")
                .param("targetDateTime", LocalDateTime.now().toString())
                .param("cursorId", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "핫 키워드로 뉴스 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("keyword")
                            .description("키워드를 입력해주세요"),
                        RequestDocumentation
                            .parameterWithName("targetDateTime")
                            .description("타입 : LocalDateTime, 조회할 날짜/시간 ex) 2023-06-30T21:30:42"),
                        RequestDocumentation
                            .parameterWithName("cursorId")
                            .description("커서 아이디, 가장 마지막에 받은 id를 넣어주세요 (기본 값은 0으로 지정됩니다.)"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 20까지 허용합니다.)"),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        PayloadDocumentation.fieldWithPath("result[].id").type(JsonFieldType.NUMBER)
                            .description("뉴스 id"),
                        PayloadDocumentation.fieldWithPath("result[].title").type(JsonFieldType.STRING)
                            .description("뉴스 제목"),
                        PayloadDocumentation.fieldWithPath("result[].thumbnailImageUrl").type(JsonFieldType.STRING)
                            .description("뉴스 이미지 링크"),
                        PayloadDocumentation.fieldWithPath("result[].newsLink").type(JsonFieldType.STRING)
                            .description("뉴스 링크"),
                        PayloadDocumentation.fieldWithPath("result[].press").type(JsonFieldType.STRING)
                            .description("언론사"),
                        PayloadDocumentation.fieldWithPath("result[].writtenDateTime").type(JsonFieldType.STRING)
                            .description("작성 시각"),
                        PayloadDocumentation.fieldWithPath("result[].type").type(JsonFieldType.STRING)
                            .description("헤드라인 뉴스인지, 일반 뉴스인지"),
                        PayloadDocumentation.fieldWithPath("result[].category").type(JsonFieldType.STRING)
                            .description("카테고리"),
                    )
                )
            )
    }
}
