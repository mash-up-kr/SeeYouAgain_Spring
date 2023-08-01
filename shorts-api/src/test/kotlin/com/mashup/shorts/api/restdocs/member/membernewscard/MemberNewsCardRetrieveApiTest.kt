package com.mashup.shorts.api.restdocs.member.membernewscard

import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.home.membernewscard.MemberNewsCardRetrieveApi
import com.mashup.shorts.domain.membernewscard.MemberNewsCardRetrieve
import com.mashup.shorts.domain.newscard.NewsCard
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
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

@WebMvcTest(MemberNewsCardRetrieveApi::class)
class MemberNewsCardRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsCardRetrieve: MemberNewsCardRetrieve

    @Test
    fun `홈 조회`() {
        // ready
        val targetDateTime = LocalDateTime.now().minusDays(1)
        val uniqueKey = "uniqueKey"
        val headerName = "Authorization"

        every { memberNewsCardRetrieve.retrieveNewsCardByMember(any(), any()) } returns (
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
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "홈 조회",
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

}