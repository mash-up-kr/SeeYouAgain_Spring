package com.mashup.shorts.api.restdocs.member.membernews

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.membernews.MemberNewsRead
import com.mashup.shorts.domain.my.membernews.MemberNewsClearApi
import com.mashup.shorts.domain.my.membernews.dto.MemberNewsReadRequest
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

@WebMvcTest(MemberNewsClearApi::class)
class MemberNewsReadApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsRead: MemberNewsRead

    @Test
    fun `뉴스 읽음 처리`() {
        every { memberNewsRead.clearNewsCard(any(), any(), any()) } returns (Unit)

        val requestBody = MemberNewsReadRequest(newsId = 1L)

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/member/news/read")
                .header("Authorization", "test-user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "뉴스 읽음 처리",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
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
