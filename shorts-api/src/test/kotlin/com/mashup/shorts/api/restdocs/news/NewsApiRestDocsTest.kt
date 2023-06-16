package com.mashup.shorts.api.restdocs.news

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.domain.news.NewsRetrieve
import com.mashup.shorts.domain.news.NewsRetrieveApi
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(NewsRetrieveApi::class)
class NewsApiRestDocsTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var newsRetrieve: NewsRetrieve

    @Test
    fun 뉴스_자세히보기() {
        // ready
        val newsId = 10L
        every { newsRetrieve.retrieveNewsLinkByNewsId(newsId) } returns ("https://shorts~")

        // execute
        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/news/{newsId}", newsId)
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "뉴스 상세 조회 (웹 뷰)",
                    pathParameters(
                        RequestDocumentation
                            .parameterWithName("newsId")
                            .description("조회할 뉴스 아이디"),
                    ),
                    responseFields(
                        fieldWithPath("status")
                            .type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        fieldWithPath("result")
                            .type(JsonFieldType.STRING)
                            .description("뉴스 링크")
                    )
                )
            )
    }
}
