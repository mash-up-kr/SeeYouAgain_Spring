package com.mashup.shorts.api.restdocs.member.memberlog

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet.Companion.pageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.memberlog.MemberAttendance
import com.mashup.shorts.domain.memberlog.MemberSharing
import com.mashup.shorts.domain.my.memberlog.MemberLogApi
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

@WebMvcTest(MemberLogApi::class)
class MemberAttendanceApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberAttendance: MemberAttendance

    @MockkBean
    private lateinit var memberSharing: MemberSharing

    @Test
    fun `유저 출석`() {
        justRun { memberAttendance.execute(any()) }

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/member/log/attendance")
                .header("Authorization", "Bearer test-user")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "유저 출석",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                    )
                )
            )
    }
}
