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
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.my.statistics.MemberStatsRetrieve
import com.mashup.shorts.domain.my.statistics.MemberStatsRetrieveApi
import com.mashup.shorts.domain.my.statistics.MemberWeeklyStats
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(MemberStatsRetrieveApi::class)
class MemberStatsRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberStatsRetrieve: MemberStatsRetrieve

    @Test
    fun `주간 숏스 통계`() {
        every { memberStatsRetrieve.retrieveMemberWeeklyStats(any(), any(), any()) } returns MemberWeeklyStats(
            weeklyShortsCnt = mapOf(Pair("7월 1주차", 1), Pair("7월 2주차", 2), Pair("7월 3주차", 3), Pair("7월 4주차", 4)),
            dateOfShortsRead = mapOf(Pair("lastWeek", listOf("2023-07-17")), Pair("thisWeek", listOf("2023-07-29", "2023-07-30")))
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/member/weekly-stats")
                .header("Authorization", "Bearer test-user")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "주간 숏스 통계",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.weeklyShortsCnt").type(JsonFieldType.OBJECT)
                            .description("주차별 숏스 읽은 개수"),
                        PayloadDocumentation.fieldWithPath("result.weeklyShortsCnt.7월 1주차").type(JsonFieldType.NUMBER)
                            .description("월 단위 주차"),
                        PayloadDocumentation.fieldWithPath("result.weeklyShortsCnt.7월 2주차").type(JsonFieldType.NUMBER)
                            .description("월 단위 주차"),
                        PayloadDocumentation.fieldWithPath("result.weeklyShortsCnt.7월 3주차").type(JsonFieldType.NUMBER)
                            .description("월 단위 주차"),
                        PayloadDocumentation.fieldWithPath("result.weeklyShortsCnt.7월 4주차").type(JsonFieldType.NUMBER)
                            .description("월 단위 주차 (이번주)"),
                        PayloadDocumentation.fieldWithPath("result.dateOfShortsRead").type(JsonFieldType.OBJECT)
                            .description("숏스 읽은 날짜 목록"),
                        PayloadDocumentation.fieldWithPath("result.dateOfShortsRead.lastWeek").type(JsonFieldType.ARRAY)
                            .description("지난주 숏스 읽은 날짜"),
                        PayloadDocumentation.fieldWithPath("result.dateOfShortsRead.thisWeek").type(JsonFieldType.ARRAY)
                            .description("이번주 숏스 읽은 날짜"),
                    )
                )
            )
    }

}
