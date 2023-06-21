package com.mashup.shorts.api.restdocs.news

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.news.NewsRetrieve
import com.mashup.shorts.domain.home.news.NewsRetrieveApi
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
}
