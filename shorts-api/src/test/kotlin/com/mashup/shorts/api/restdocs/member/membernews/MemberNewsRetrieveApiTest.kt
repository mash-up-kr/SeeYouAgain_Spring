package com.mashup.shorts.api.restdocs.member.membernews

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.membernews.MemberNewsRetrieve
import com.mashup.shorts.domain.my.membernews.MemberNewsRetrieveApi
import com.mashup.shorts.domain.news.News
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
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@WebMvcTest(MemberNewsRetrieveApi::class)
class MemberNewsRetrieveApiTest : ApiDocsTestBase() {
    @MockkBean
    private lateinit var memberNewsRetrieve: MemberNewsRetrieve

    @Test
    fun `키워드로 조회하여 저장한 모든 뉴스 조회`() {
        val cursorWrittenDateTime = "2023.06.15. 오후 3:38"
        val size = 10
        val pivot = "ASC"
        val uniqueKey = "uniqueKey"
        val headerName = "Authorization"

        every { memberNewsRetrieve.retrieveMemberNewsBySorting(any(), any(), any(), any(), any()) } returns (
                listOf(
                    News(
                        title = "뉴스 제목",
                        content = "뉴스 내용",
                        thumbnailImageUrl = "이미지 링크",
                        newsLink = "뉴스 링크",
                        writtenDateTime = "2023.06.29. 오전 11:41",
                        type = "타입",
                        press = "언론사",
                        crawledCount = 1,
                        category = Category(CategoryName.SCIENCE)
                    )
                ))
        every { memberNewsRetrieve.retrieveMemberNewsCount(any()) } returns (1234)
        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/member-news/keyword")
                .header(headerName, uniqueKey)
                .param("cursorWrittenDateTime", cursorWrittenDateTime)
                .param("size", size.toString())
                .param("pivot", pivot)
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "키워드로 조회하여 저장한 모든 뉴스 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("cursorWrittenDateTime")
                            .description("커서 지정 값 ex) 2023.06.15. 오후 3:38 와 같이 입력해 주시고, 첫 페이지 요청 시 빈 값으로 요청해주세요 (빈 문자열 X)"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 20까지 허용합니다.)"),
                        RequestDocumentation
                            .parameterWithName("pivot")
                            .description("<필수값> 정렬 기준 [ASC, DESC] 둘 중 하나만 허용합니다. ASC는 오래된 순, DESC는 최신 순 입니다."),
                    ),
                    responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        PayloadDocumentation.fieldWithPath("result.savedNewsCount").type(JsonFieldType.NUMBER)
                            .description("키워드에서 저장한 뉴스 갯수"),
                        PayloadDocumentation.subsectionWithPath("result.memberNewsResponse[]")
                            .description("멤버 뉴스 응답"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].id")
                            .type(JsonFieldType.NUMBER)
                            .description("뉴스 id"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].title")
                            .type(JsonFieldType.STRING)
                            .description("뉴스 제목"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].thumbnailImageUrl")
                            .type(JsonFieldType.STRING)
                            .description("뉴스 이미지 링크"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].newsLink")
                            .type(JsonFieldType.STRING)
                            .description("뉴스 링크"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].press")
                            .type(JsonFieldType.STRING)
                            .description("언론사"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].writtenDateTime")
                            .type(JsonFieldType.STRING)
                            .description("작성 시각 ex) 2023.06.15. 오후 3:38"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].type")
                            .type(JsonFieldType.STRING)
                            .description("응답 예시 : [HEADLINE, NORMAL],헤드라인 뉴스인지 일반 뉴스인지 구분하는 필드입니다."),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].category")
                            .type(JsonFieldType.STRING)
                            .description("카테고리"),
                    )
                )
            )
    }

    @Test
    fun `뉴스 카드에서 저장한 모든 뉴스 조회`() {
        val cursorWrittenDateTime = "2023.06.15. 오후 3:38"
        val size = 10
        val pivot = "ASC"
        val uniqueKey = "uniqueKey"
        val headerName = "Authorization"

        every { memberNewsRetrieve.retrieveMemberNewsBySorting(any(), any(), any(), any(), any()) } returns (
                listOf(
                    News(
                        title = "뉴스 제목",
                        content = "뉴스 내용",
                        thumbnailImageUrl = "이미지 링크",
                        newsLink = "뉴스 링크",
                        writtenDateTime = "2023.06.29. 오전 11:41",
                        type = "타입",
                        press = "언론사",
                        crawledCount = 1,
                        category = Category(CategoryName.SCIENCE)
                    )
                ))
        every { memberNewsRetrieve.retrieveMemberNewsCount(any()) } returns (1234)
        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/member-news/newscard")
                .header(headerName, uniqueKey)
                .param("cursorWrittenDateTime", cursorWrittenDateTime)
                .param("size", size.toString())
                .param("pivot", pivot)
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "뉴스 카드에서 저장한 모든 뉴스 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("cursorWrittenDateTime")
                            .description("커서 지정 값 ex) 2023.06.15. 오후 3:38 와 같이 입력해 주시고, 첫 페이지 요청 시 빈 값으로 요청해주세요 (빈 문자열 X)"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 20까지 허용합니다.)"),
                        RequestDocumentation
                            .parameterWithName("pivot")
                            .description("<필수값> 정렬 기준 [ASC, DESC] 둘 중 하나만 허용합니다. ASC는 오래된 순, DESC는 최신 순 입니다."),
                    ),
                    responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        PayloadDocumentation.fieldWithPath("result.savedNewsCount").type(JsonFieldType.NUMBER)
                            .description("카드뉴스에서 저장한 뉴스 갯수"),
                        PayloadDocumentation.subsectionWithPath("result.memberNewsResponse[]")
                            .description("멤버 뉴스 응답"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].id")
                            .type(JsonFieldType.NUMBER)
                            .description("뉴스 id"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].title")
                            .type(JsonFieldType.STRING)
                            .description("뉴스 제목"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].thumbnailImageUrl")
                            .type(JsonFieldType.STRING)
                            .description("뉴스 이미지 링크"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].newsLink")
                            .type(JsonFieldType.STRING)
                            .description("뉴스 링크"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].press")
                            .type(JsonFieldType.STRING)
                            .description("언론사"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].writtenDateTime")
                            .type(JsonFieldType.STRING)
                            .description("작성 시각 ex) 2023.06.15. 오후 3:38"),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].type")
                            .type(JsonFieldType.STRING)
                            .description("응답 예시 : [HEADLINE, NORMAL],헤드라인 뉴스인지 일반 뉴스인지 구분하는 필드입니다."),
                        PayloadDocumentation.fieldWithPath("result.memberNewsResponse[].category")
                            .type(JsonFieldType.STRING)
                            .description("카테고리"),
                    )
                )
            )
    }
}
