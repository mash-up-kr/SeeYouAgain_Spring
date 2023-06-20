package com.mashup.shorts.api.restdocs.member.my

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.member.my.MemberInfo
import com.mashup.shorts.domain.member.my.MemberInfoRetrieve
import com.mashup.shorts.domain.member.my.MemberInfoRetrieveApi
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(MemberInfoRetrieveApi::class)
class MemberInfoRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberInfoRetrieve: MemberInfoRetrieve

    @Test
    fun `내 정보 조회`() {
        every { memberInfoRetrieve.retrieveMemberInfo(any(), any()) } returns MemberInfo(
            nickname = "똑똑한여행가",
            joinPeriod = 114,
            totalShortsThisMonth = 56,
            todayShorts = 5,
            savedShorts = 56
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/member/info")
                .header("Authorization", "Bearer test-user")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "내 정보 조회",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.nickname").type(JsonFieldType.STRING).description("사용자 닉네임"),
                        PayloadDocumentation.fieldWithPath("result.joinPeriod").type(JsonFieldType.NUMBER).description("가입 기간"),
                        PayloadDocumentation.fieldWithPath("result.totalShortsThisMonth").type(JsonFieldType.NUMBER).description("이번 달에 '다 읽었어요' 누른 횟수"),
                        PayloadDocumentation.fieldWithPath("result.todayShorts").type(JsonFieldType.NUMBER).description("오늘의 숏스 개수"),
                        PayloadDocumentation.fieldWithPath("result.savedShorts").type(JsonFieldType.NUMBER).description("오래 간직할 숏스 개수")
                    )
                )
            )
    }
}
