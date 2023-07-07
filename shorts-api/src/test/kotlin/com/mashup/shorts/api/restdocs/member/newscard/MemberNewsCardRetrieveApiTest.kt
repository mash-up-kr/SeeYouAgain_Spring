package com.mashup.shorts.api.restdocs.member.newscard

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRetrieve
import com.mashup.shorts.domain.membernewscard.dtomapper.MemberTodayShorts
import com.mashup.shorts.domain.my.membernewscard.MemberNewsCardRetrieveApi
import com.mashup.shorts.domain.newscard.NewsCard
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(MemberNewsCardRetrieveApi::class)
class MemberNewsCardRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsCardRetrieve: MemberNewsCardRetrieve

    @Test
    fun `홈 조회`() {
        // ready
        val targetDateTime = LocalDateTime.now().minusDays(1)
        val cursorId = 0L
        val size = 10
        val uniqueKey = "uniqueKey"
        val headerName = "Authorization"

        every { memberNewsCardRetrieve.retrieveNewsCardByMember(any(), any(), any(), any()) } returns (
            listOf(
                NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드1, 테스트 키워드2, 테스트 키워드3, 테스트 키워드4,",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                )
            )
            )

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/member-news-card")
                .header(headerName, uniqueKey)
                .param("targetDateTime", targetDateTime.toString())
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "숏스 모두 불러오기",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    queryParameters(
                        RequestDocumentation
                            .parameterWithName("targetDateTime")
                            .description("요청 날짜 및 시간"),
                        RequestDocumentation
                            .parameterWithName("cursorId")
                            .description("커서 아이디, 가장 마지막에 받은 id를 넣어주세요 (기본 값은 0으로 지정됩니다.)"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 20까지 허용합니다.)"),
                    ),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        fieldWithPath("result[].id").type(JsonFieldType.NUMBER)
                            .description("숏스 id"),
                        fieldWithPath("result[].keywords").type(JsonFieldType.STRING)
                            .description("키워드"),
                        fieldWithPath("result[].category").type(JsonFieldType.STRING)
                            .description("카테고리"),
                        fieldWithPath("result[].crawledDateTime").type(JsonFieldType.STRING)
                            .description("크롤링 된 시각 ex) 2023-06-30T21:30:42"),

                    )
                )
            )
    }

    @Test
    fun `저장한 오늘의 숏스 조회`() {
        // ready
        val cursorId = 0L
        val size = 10
        val uniqueKey = "uniqueKey"
        val headerName = "Authorization"

        every { memberNewsCardRetrieve.retrieveSavedNewsCardByMember(any(), any(), any()) } returns (
            MemberTodayShorts(
                numberOfShorts = 1234,
                numberOfReadShorts = 5678,
                memberShorts = listOf(
                    NewsCard(
                        category = Category(CategoryName.POLITICS),
                        multipleNews = "1, 2, 3, 4, 5",
                        keywords = "테스트 키워드1, 테스트 키워드2, 테스트 키워드3, 테스트 키워드4,",
                        createdAt = LocalDateTime.now(),
                        modifiedAt = LocalDateTime.now(),
                    )
                )
            )
            )

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/member-news-card/saved")
                .header(headerName, uniqueKey)
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "저장한 오늘의 숏스 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    queryParameters(
                        RequestDocumentation
                            .parameterWithName("cursorId")
                            .description("커서 아이디, 가장 마지막에 받은 id를 넣어주세요 (기본 값은 0으로 지정됩니다.)"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 20까지 허용합니다.)"),
                    ),
                    responseFields(
                        fieldWithPath("status")
                            .type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        fieldWithPath("result.numberOfShorts")
                            .type(JsonFieldType.NUMBER)
                            .description("오늘 저장한 숏스 갯수"),
                        fieldWithPath("result.numberOfReadShorts")
                            .type(JsonFieldType.NUMBER)
                            .description("오늘 읽은 숏스 갯수"),
                        fieldWithPath("result.memberShorts[].id")
                            .type(JsonFieldType.NUMBER)
                            .description("숏스 id"),
                        fieldWithPath("result.memberShorts[].keywords")
                            .type(JsonFieldType.STRING)
                            .description("숏스 키워드"),
                        fieldWithPath("result.memberShorts[].category")
                            .type(JsonFieldType.STRING)
                            .description("숏스 카테고리"),
                        fieldWithPath("result.memberShorts[].crawledDateTime")
                            .type(JsonFieldType.STRING)
                            .description("크롤링 된 시각 ex) 2023-06-30T21:30:42"),
                    )
                )
            )
    }
}
