package com.mashup.shorts.api.restdocs.member.newscard

import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardApi
import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardClear
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardClearRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(MemberNewsCardApi::class)
class MemberNewsCardApiRestDocsTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsCardClear: MemberNewsCardClear

    @Test
    fun 뉴스카드_다_읽었어염() {
        every { memberNewsCardClear.clearMemberNewsCard(any(), any()) } returns (any())

        // ready
        val url = "/v1/member-news-card"
        val memberId = 1L
        val newsCardId = 1L
        val body = MemberNewsCardClearRequest(memberId, newsCardId)

        // execute
        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(url)
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "뉴스 다 읽었어요 API",
                    requestFields(
                        fieldWithPath("memberId").description("멤버 id"),
                        fieldWithPath("newsCardId").description("뉴스카드 id"),
                    )
                ),
            )
    }
}
