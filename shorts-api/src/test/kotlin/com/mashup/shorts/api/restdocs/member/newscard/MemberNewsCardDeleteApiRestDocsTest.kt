package com.mashup.shorts.api.restdocs.member.newscard

import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.JsonFieldType.NUMBER
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.membernewscard.MemberNewsCardDelete
import com.mashup.shorts.domain.my.membernewscard.MemberNewsCardDeleteApi
import com.mashup.shorts.domain.my.membernewscard.dto.MemberNewsCardBulkDeleteRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(MemberNewsCardDeleteApi::class)
class MemberNewsCardDeleteApiRestDocsTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsCardDelete: MemberNewsCardDelete

    @Test
    fun 뉴스카드_다_읽었어요() {
        every { memberNewsCardDelete.clearMemberNewsCard(any(), any()) } returns (
            mapOf("shortsCount" to 1234)
        )

        // ready
        val url = "/v1/member-news-card"

        // execute
        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(url)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "뉴스카드 다 읽었어요 (오늘의 숏스 모두 삭제 후 숏스 카운트 + 1)",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    responseFields(
                        fieldWithPath("status").type(NUMBER).description("API 성공 여부"),
                        fieldWithPath("result.shortsCount").type(NUMBER).description("읽은 숏스 갯수"),
                    )
                )
            )
    }

    @Test
    fun `오늘의 숏스 삭제`() {
        // ready
        val url = "/v1/member-news-card"
        val uniqueKey = "uniqueKey"
        val headerName = "Authorization"
        val requestBody = MemberNewsCardBulkDeleteRequest(listOf(1L, 2L, 3L))

        every { memberNewsCardDelete.bulkDeleteMemberNewsCard(any(), any()) } returns (any())

        // execute
        val response = mockMvc.perform(
            RestDocumentationRequestBuilders.post(url)
                .header(headerName, uniqueKey)
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "오늘의 숏스 삭제",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    requestHeaders(
                        HeaderDocumentation
                            .headerWithName("Authorization")
                            .description("사용자 식별자 id")
                    ),
                    requestFields(
                        fieldWithPath("newsCardIds").type(JsonFieldType.ARRAY).description("삭제할 오늘의 숏스(뉴스카드) id 리스트")
                    ),
                    responseFields(
                        fieldWithPath("status").type(NUMBER).description("API HTTP Status 값")
                    ),
                )
            )
    }
}
