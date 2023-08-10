package com.mashup.shorts.api.restdocs.member.my

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.member.MemberNicknameModifier
import com.mashup.shorts.domain.my.member.MemberNicknameUpdateApi
import com.mashup.shorts.domain.my.member.dto.MemberNicknameUpdateRequest
import com.ninjasquad.springmockk.MockkBean
import io.mockk.justRun
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(MemberNicknameUpdateApi::class)
class MemberNicknameUpdateApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNicknameModifier: MemberNicknameModifier

    @Test
    fun `닉네임 변경`() {
        justRun { memberNicknameModifier.modifyMemberNickname(any(), any()) }

        val requestBody = MemberNicknameUpdateRequest(
            nickname = "닉네임"
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/v1/member/nickname")
                .header("Authorization", "Bearer test-user")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "닉네임 변경",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("nickname")
                            .type(JsonFieldType.STRING)
                            .description("닉네임"),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                    )
                )
            )
    }

}
