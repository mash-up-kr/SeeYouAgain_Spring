package com.mashup.shorts.api.restdocs.member.membercompany

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet.Companion.pageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.member.Company
import com.mashup.shorts.domain.membercompany.MemberCompanyCreate
import com.mashup.shorts.domain.my.member.dto.MemberCompanyCreateRequest
import com.mashup.shorts.domain.my.membercompany.MemberCompanyCreateApi
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

@WebMvcTest(MemberCompanyCreateApi::class)
class MemberCompanyCreateApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberCompanyCreate: MemberCompanyCreate

    @Test
    fun `관심 회사 추가`() {
        justRun { memberCompanyCreate.createMemberCompany(any(), any()) }

        val body = MemberCompanyCreateRequest(
            companies = listOf(Company.NAVER, Company.COUPANG)
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.post("/v1/member/company")
                .header("Authorization", "Bearer test-user")
                .content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "관심 회사 추가",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("companies")
                            .type(JsonFieldType.ARRAY)
                            .description("관심 회사 추가"),
                        PayloadDocumentation.fieldWithPath("companies[]")
                            .type(JsonFieldType.ARRAY)
                            .description("관심 회사 추가 [NAVER, KAKAO, LINE, COUPANG, WOOAH, CARROT, TOSS, SAMSUNG, HYUNDAI, CJ, KOREA_ELEC, LG_ELEC, KOREA_GAS, SK_HYNICS]"),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                    )
                )
            )
    }
}
