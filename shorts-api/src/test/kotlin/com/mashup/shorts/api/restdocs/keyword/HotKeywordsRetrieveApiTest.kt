package com.mashup.shorts.api.restdocs.keyword

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.hot.keyword.HotKeywordsRetrieveApi
import com.mashup.shorts.domain.keyword.HotKeywordRetrieve
import com.mashup.shorts.domain.keyword.KeywordRanking
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(HotKeywordsRetrieveApi::class)
class HotKeywordsRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var hotKeywordRetrieve: HotKeywordRetrieve

    @Test
    fun `핫 키워드 조회`() {
        every { hotKeywordRetrieve.retrieveHotKeywords(any()) } returns (KeywordRanking(
            createdAt = "2023-06-30T10:12:16.913821",
            ranking = listOf("키워드1", "키워드2", "키워드3")
        ))

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/hot-keywords")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "핫 키워드 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                        fieldWithPath("result.createdAt").type(JsonFieldType.STRING)
                            .description("키워드 시간대 ex) 2023-06-30T21:30:42"),
                        fieldWithPath("result.ranking").type(JsonFieldType.ARRAY)
                            .description("핫 키워드 순위 (1위 ~ 10위)")
                    )
                )
            )
    }
}
