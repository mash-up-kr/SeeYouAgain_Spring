package com.mashup.shorts.api.restdocs.member.my

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet.Companion.pageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.my.memberstatistics.MemberStatisticsRetrieve
import com.mashup.shorts.domain.my.memberstatistics.MemberStatisticsRetrieveApi
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@WebMvcTest(MemberStatisticsRetrieveApi::class)
class MemberStatisticsRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberStatisticsRetrieve: MemberStatisticsRetrieve

    @Test
    fun `멤버 숏스 통계 조회`() {
        every {
            memberStatisticsRetrieve.retrieveMemberStatisticsByMemberAndTargetDate(
            any(),
            any()
        ) }.returns(
                mutableMapOf(
                    CategoryName.POLITICS.name to 1L,
                    CategoryName.ECONOMIC.name to 2L,
                    CategoryName.SOCIETY.name to 3L,
                    CategoryName.CULTURE.name to 4L,
                    CategoryName.WORLD.name to 5L,
                    CategoryName.SCIENCE.name to 6L,
                )
            )

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/member/statistics")
                .header("Authorization", "Bearer test-user")
                .param("targetDateTime", LocalDateTime.now().toString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        result
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "멤버 숏스 통계 조회",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    pageHeaderSnippet(),
                    requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("targetDateTime")
                            .description("LocalDateTime 타입, 조회할 날짜를 입력해주세요")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.targetDateTime").type(JsonFieldType.STRING)
                            .description("어떤 월에 대한 통계인지?"),
                        PayloadDocumentation.fieldWithPath("result.statistics").type(JsonFieldType.OBJECT)
                            .description("통계"),
                        PayloadDocumentation.fieldWithPath("result.statistics.POLITICS").type(JsonFieldType.NUMBER)
                            .description("정치 카테고리 갯수"),
                        PayloadDocumentation.fieldWithPath("result.statistics.ECONOMIC").type(JsonFieldType.NUMBER)
                            .description("경제 카테고리 갯수"),
                        PayloadDocumentation.fieldWithPath("result.statistics.SOCIETY").type(JsonFieldType.NUMBER)
                            .description("사회 카테고리 갯수"),
                        PayloadDocumentation.fieldWithPath("result.statistics.CULTURE").type(JsonFieldType.NUMBER)
                            .description("생활/문화 카테고리 갯수"),
                        PayloadDocumentation.fieldWithPath("result.statistics.WORLD").type(JsonFieldType.NUMBER)
                            .description("세계 카테고리 갯수"),
                        PayloadDocumentation.fieldWithPath("result.statistics.SCIENCE").type(JsonFieldType.NUMBER)
                            .description("IT/과학 카테고리 갯수"),
                    )
                )
            )
    }
}
