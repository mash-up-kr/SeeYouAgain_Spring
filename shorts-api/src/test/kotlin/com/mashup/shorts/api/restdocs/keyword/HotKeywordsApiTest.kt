package com.mashup.shorts.api.restdocs.keyword

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.keyword.HotKeywordsApi

@WebMvcTest(HotKeywordsApi::class)
class HotKeywordsApiTest : ApiDocsTestBase() {

    @Test
    fun `핫 키워드 조회`() {
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
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.createdAt").type(JsonFieldType.STRING).description("핫 키워드 생성 시간"),
                        PayloadDocumentation.fieldWithPath("result.ranking").type(JsonFieldType.ARRAY).description("핫 키워드 순위 (1위 ~ 10위)")
                    )
                )
            )
    }
}
