//package com.mashup.shorts.api.restdocs.member.newscard
//
//import org.junit.jupiter.api.Test
//import org.mockito.ArgumentMatchers.any
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
//import org.springframework.http.MediaType.APPLICATION_JSON
//import org.springframework.restdocs.headers.HeaderDocumentation
//import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
//import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
//import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
//import org.springframework.restdocs.payload.JsonFieldType
//import org.springframework.restdocs.payload.JsonFieldType.NULL
//import org.springframework.restdocs.payload.JsonFieldType.NUMBER
//import org.springframework.restdocs.payload.PayloadDocumentation
//import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
//import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
//import org.springframework.restdocs.request.RequestDocumentation
//import org.springframework.restdocs.request.RequestDocumentation.pathParameters
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers
//import com.mashup.shorts.api.ApiDocsTestBase
//import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardApi
//import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardClear
//import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardClearRequest
//import com.ninjasquad.springmockk.MockkBean
//import io.mockk.every
//
//@WebMvcTest(MemberNewsCardApi::class)
//class MemberNewsCardApiRestDocsTest : ApiDocsTestBase() {
//
//    @MockkBean
//    private lateinit var memberNewsCardClear: MemberNewsCardClear
//
//    @Test
//    fun 뉴스카드_다_읽었어요() {
//        every { memberNewsCardClear.clearMemberNewsCard(any(), any()) } returns (1234)
//
//        // ready
//        val url = "/v1/member-news-card"
//        val memberId = 1L
//        val newsCardId = 1L
//        val body = MemberNewsCardClearRequest(memberId, newsCardId)
//
//        // execute
//        val response = mockMvc.perform(
//            RestDocumentationRequestBuilders
//                .delete(url)
//                .content(objectMapper.writeValueAsString(body))
//                .contentType(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//        )
//
//        response.andExpect(MockMvcResultMatchers.status().isOk)
//            .andDo(
//                document(
//                    "뉴스카드 다 읽었어요 (오늘 읽을 모든 숏스 삭제)",
//                    requestFields(
//                        fieldWithPath("memberId").description("멤버 id"),
//                        fieldWithPath("newsCardId").description("뉴스카드 id"),
//                    ),
//                    PayloadDocumentation.responseFields(
//                        fieldWithPath("status").type(NUMBER).description("API 성공 여부"),
//                        fieldWithPath("result.shortsCount").type(NUMBER).description("읽은 숏스 갯수"),
//                    )
//                )
//            )
//    }
//
//    @Test
//    fun 뉴스카드_삭제() {
//        // ready
//        val url = "/v1/member-news-card/{newsCardId}"
//        val newsCardId = 1L
//        val uniqueKey = "uniqueKey"
//        val headerName = "Authorization"
//
//        every { memberNewsCardClear.deleteMemberNewsCard(any(), any()) } returns (any())
//
//        // execute
//        val response = mockMvc.perform(
//            RestDocumentationRequestBuilders.delete(url, newsCardId)
//                .header(headerName, uniqueKey)
//                .contentType(APPLICATION_JSON)
//                .accept(APPLICATION_JSON)
//        )
//
//        response.andExpect(MockMvcResultMatchers.status().isOk)
//            .andDo(
//                document(
//                    "오늘의 숏스 단일 삭제",
//                    requestHeaders(
//                        HeaderDocumentation
//                            .headerWithName("Authorization")
//                            .description("사용자 식별자 id")
//                    ),
//                    pathParameters(
//                        RequestDocumentation
//                            .parameterWithName("newsCardId")
//                            .description("뉴스 카드 id"),
//                    ),
//                    PayloadDocumentation.responseFields(
//                        fieldWithPath("status").type(NUMBER).description("API 성공 여부"),
//                        fieldWithPath("result").type(NULL).description("응답 데이터"),
//                    )
//                )
//            )
//    }
//}
