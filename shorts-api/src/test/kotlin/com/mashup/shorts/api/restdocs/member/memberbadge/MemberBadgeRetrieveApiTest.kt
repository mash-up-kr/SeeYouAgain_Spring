package com.mashup.shorts.api.restdocs.member.memberbadge

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet.Companion.pageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.memberbadge.MemberBadge
import com.mashup.shorts.domain.memberbadge.MemberBadgeRetrieve
import com.mashup.shorts.domain.my.memberbadge.MemberBadgeRetrieveApi
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(MemberBadgeRetrieveApi::class)
class MemberBadgeRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberBadgeRetrieve: MemberBadgeRetrieve

    @Test
    fun `유저 뱃지 조회`() {
        every { memberBadgeRetrieve.retrieveMemberBadge(any()) } returns MemberBadge(
            member = Member("testUniuqe", "testNickname", "")
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/member/badge")
                .header("Authorization", "Bearer test-user")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "유저 뱃지 조회",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.threeDaysContinuousAttendance")
                            .type(JsonFieldType.BOOLEAN)
                            .description("작심삼일 뱃지 획득 여부"),
                        PayloadDocumentation.fieldWithPath("result.tenDaysContinuousAttendance")
                            .type(JsonFieldType.BOOLEAN)
                            .description("단골손님 뱃지 획득 여부"),
                        PayloadDocumentation.fieldWithPath("result.explorer")
                            .type(JsonFieldType.BOOLEAN)
                            .description("세상 탐험가 뱃지 획득 여부"),
                        PayloadDocumentation.fieldWithPath("result.kingOfSharing")
                            .type(JsonFieldType.BOOLEAN)
                            .description("뿌듯한 첫 공유 뱃지 획득 여부"),
                        PayloadDocumentation.fieldWithPath("result.firstAllReadShorts")
                            .type(JsonFieldType.BOOLEAN)
                            .description("시작이 반 뱃지 획득 여부"),
                        PayloadDocumentation.fieldWithPath("result.firstOldShortsSaving")
                            .type(JsonFieldType.BOOLEAN)
                            .description("오래 간직할 지식 뱃지 획득 여부"),
                        PayloadDocumentation.fieldWithPath("result.changeMode")
                            .type(JsonFieldType.BOOLEAN)
                            .description("취향존중 뱃지 획득 여부"),
                    )
                )
            )
    }
}
