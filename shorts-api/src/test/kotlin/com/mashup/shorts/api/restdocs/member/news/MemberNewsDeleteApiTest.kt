package com.mashup.shorts.api.restdocs.member.news

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.member.membernews.MemberNewsDelete
import com.mashup.shorts.domain.member.membernews.MemberNewsDeleteApi
import com.mashup.shorts.domain.member.membernews.dto.MemberNewsDeleteBulkRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(MemberNewsDeleteApi::class)
class MemberNewsDeleteApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsDelete: MemberNewsDelete

    @Test
    fun `오래 간직할 뉴스 삭제`() {
        every { memberNewsDelete.deleteMemberNews(any(), any()) } returns(Unit)

        val requestBody = MemberNewsDeleteBulkRequest(listOf(1L, 2L, 3L))

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/member/news/bulk-delete")
                .header("Authorization", "Bearer test-user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "오래 간직할 뉴스 삭제",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("newsIds").type(JsonFieldType.ARRAY).description("삭제할 뉴스 id 리스트")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("API HTTP Status 값")
                    )
                )
            )
    }
}
