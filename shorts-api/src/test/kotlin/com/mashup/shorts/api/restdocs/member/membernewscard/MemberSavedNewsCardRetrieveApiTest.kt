package com.mashup.shorts.api.restdocs.member.membernewscard

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRetrieve
import com.mashup.shorts.domain.my.membernewscard.MemberSavedNewsCardRetrieveApi
import com.mashup.shorts.domain.newscard.NewsCard
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@WebMvcTest(MemberSavedNewsCardRetrieveApi::class)
class MemberSavedNewsCardRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsCardRetrieve: MemberNewsCardRetrieve

    @Test
    fun `저장한 뉴스카드 조회`() {

        val cursorId = 1L
        val size = 10
        val pivot = "ASC"

        every {
            memberNewsCardRetrieve.retrieveSavedNewsCardByMember(
                any(),
                any(),
                any(),
                any()
            )
        } returns (
                Pair(
                    listOf(
                        NewsCard(
                            category = Category(CategoryName.SCIENCE),
                            multipleNews = "1, 2, 3, 4, 5, 6",
                            keywords = "TEST",
                            createdAt = LocalDateTime.now(),
                            modifiedAt = LocalDateTime.now()
                        ),
                        NewsCard(
                            category = Category(CategoryName.POLITICS),
                            multipleNews = "11, 22, 33, 44, 55, 66",
                            keywords = "TEST Keyword",
                            createdAt = LocalDateTime.now(),
                            modifiedAt = LocalDateTime.now()
                        ),
                    ),
                    2
                ))
        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/member/news-card/saved")
                .header("Authorization", "test-user")
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .param("pivot", pivot)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "저장한 뉴스카드 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation.headerWithName("Authorization").description("사용자 식별자 id")
                    ),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("cursorId")
                            .description("커서 id"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 20까지 허용합니다.)"),
                        RequestDocumentation
                            .parameterWithName("pivot")
                            .description("<필수값> 정렬 기준 [ASC, DESC] 둘 중 하나만 허용합니다. ASC는 오래된 순, DESC는 최신 순 입니다."),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status")
                            .type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.numberOfNewsCard")
                            .type(JsonFieldType.NUMBER)
                            .description("저장한 뉴스카드 갯수"),
                        PayloadDocumentation
                            .subsectionWithPath("result.memberShorts[]")
                            .description("멤버 뉴스 응답"),
                        PayloadDocumentation.fieldWithPath("result.memberShorts[].id")
                            .type(JsonFieldType.NUMBER)
                            .description("뉴스카드 id"),
                        PayloadDocumentation.fieldWithPath("result.memberShorts[].keywords")
                            .type(JsonFieldType.STRING)
                            .description("키워드"),
                        PayloadDocumentation.fieldWithPath("result.memberShorts[].category")
                            .type(JsonFieldType.STRING)
                            .description("카테고리"),
                        PayloadDocumentation.fieldWithPath("result.memberShorts[].crawledDateTime")
                            .type(JsonFieldType.STRING)
                            .description("크롤링된 시각"),
                    )
                )
            )
    }
}