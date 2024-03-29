package com.mashup.shorts.api.restdocs.member.membernewscard

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.home.membernewscard.MemberNewsCardCreateApi
import com.mashup.shorts.domain.home.membernewscard.dto.MemberNewsCardCreateRequest
import com.mashup.shorts.domain.membernewscard.MemberNewsCardCreate
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

@WebMvcTest(MemberNewsCardCreateApi::class)
class MemberNewsCardCreateApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsCardCreate: MemberNewsCardCreate

    @Test
    fun `뉴스카드 저장`() {
        every { memberNewsCardCreate.createMemberNewsCard(any(), any()) } returns (Unit)

        val requestBody = MemberNewsCardCreateRequest(newsCardId = 1L)

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/member/news-card")
                .header("Authorization", "test-user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "뉴스카드 저장",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("newsCardId").type(JsonFieldType.NUMBER).description("뉴스 id")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값")
                    )
                )
            )
    }
}