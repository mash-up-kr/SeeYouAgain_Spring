//package com.mashup.shorts.api.restdocs.member.newscard
//
//import org.junit.jupiter.api.Test
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.http.MediaType
//import org.springframework.restdocs.headers.HeaderDocumentation
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
//import org.springframework.restdocs.payload.JsonFieldType
//import org.springframework.restdocs.payload.PayloadDocumentation
//import org.springframework.test.web.servlet.result.MockMvcResultHandlers
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import com.mashup.shorts.api.ApiDocsTestBase
//import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardCreate
//import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardCreateApi
//import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardCreateRequest
//import com.ninjasquad.springmockk.MockkBean
//import io.mockk.every
//
//@WebMvcTest(MemberNewsCardCreateApi::class)
//class MemberNewsCardCreateApiTest : ApiDocsTestBase() {
//
//    @MockkBean
//    private lateinit var memberNewsCardCreate: MemberNewsCardCreate
//
//    @Test
//    fun `오늘의 숏스 추가`() {
//        every { memberNewsCardCreate.createMemberNewsCard(any(), any()) } returns(Unit)
//
//        val requestBody = MemberNewsCardCreateRequest(newsCardId = 1L)
//
//        mockMvc.perform(
//            RestDocumentationRequestBuilders.post("/v1/member/news-card")
//                .header("Authorization", "test-user")
//                .content(objectMapper.writeValueAsString(requestBody))
//                .contentType(MediaType.APPLICATION_JSON)
//        ).andDo(MockMvcResultHandlers.print())
//            .andExpect(MockMvcResultMatchers.status().isOk)
//            .andDo(
//                MockMvcRestDocumentation.document(
//                    "오늘의 숏스 추가",
//                    HeaderDocumentation.requestHeaders(
//                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
//                    ),
//                    PayloadDocumentation.requestFields(
//                        PayloadDocumentation.fieldWithPath("newsCardId").type(JsonFieldType.NUMBER).description("뉴스카드 id")
//                    ),
//                    PayloadDocumentation.responseFields(
//                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("API HTTP Status 값"),
//                        PayloadDocumentation.fieldWithPath("result").type(JsonFieldType.NULL).description("반환 결과 없음")
//                    )
//                )
//            )
//    }
//}
