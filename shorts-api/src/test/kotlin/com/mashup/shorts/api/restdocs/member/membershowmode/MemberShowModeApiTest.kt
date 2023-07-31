package com.mashup.shorts.api.restdocs.member.membershowmode

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet.Companion.pageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.member.ShowMode
import com.mashup.shorts.domain.membershowmode.MemberShowModeUpdate
import com.mashup.shorts.domain.my.info.dto.MemberChangeShowModeRequest
import com.mashup.shorts.domain.my.membershowmode.MemberShowModeApi
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

@WebMvcTest(MemberShowModeApi::class)
class MemberShowModeApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberShowModeUpdate: MemberShowModeUpdate

    @Test
    fun `유저 조회 모드 변경`() {
        justRun { memberShowModeUpdate.updateMemberShowMode(any(), any()) }

        val body = MemberChangeShowModeRequest(
            showMode = ShowMode.RECRUIT
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.patch("/v1/member/show-mode")
                .header("Authorization", "Bearer test-user")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "유저 조회 모드 변경",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("showMode")
                            .type(JsonFieldType.STRING)
                            .description("조회 모드를 넣어주세요 [NORMAL, RECRUIT, INVESTMENT, SWITCH_JOB]"),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                    )
                )
            )
    }
}
