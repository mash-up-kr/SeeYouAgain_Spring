package com.mashup.shorts.api.restdocs.member.membernews

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.membernews.MemberNewsCreate
import com.mashup.shorts.domain.my.membernews.MemberNewsCreateApi
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsCreateRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
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

@WebMvcTest(MemberNewsCreateApi::class)
class MemberNewsCreateApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsCreate: MemberNewsCreate

    @Test
    fun `키워드를 통해 조회한 뉴스 저장`() {
        every { memberNewsCreate.createMemberNewsByKeyword(any(), any()) } returns (Unit)

        val requestBody = MemberNewsCreateRequest(newsId = 1L)

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/member/news/keyword")
                .header("Authorization", "test-user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "키워드를 통해 조회한 뉴스 저장",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("newsId").type(JsonFieldType.NUMBER).description("뉴스 id")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값")
                    )
                )
            )
    }

    @Test
    fun `뉴스카드에서 조회한 뉴스 저장`() {
        every { memberNewsCreate.createMemberNewsByNewsCard(any(), any()) } returns (Unit)

        val requestBody = MemberNewsCreateRequest(newsId = 1L)

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/member/news/newscard")
                .header("Authorization", "test-user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "뉴스카드에서 조회한 뉴스 저장",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("newsId").type(JsonFieldType.NUMBER).description("뉴스 id")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값")
                    )
                )
            )
    }
}
