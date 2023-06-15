package com.mashup.shorts.api.restdocs.member.newscard

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.JsonFieldType.NULL
import org.springframework.restdocs.payload.JsonFieldType.NUMBER
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardApi
import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardClear
import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardRetrieve
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardClearRequest
import com.mashup.shorts.domain.newscard.NewsCard
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(MemberNewsCardApi::class)
class MemberNewsCardApiRestDocsTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberNewsCardClear: MemberNewsCardClear

    @MockkBean
    private lateinit var memberNewsCardRetrieve: MemberNewsCardRetrieve

    @Test
    fun 뉴스카드_모두_조회() {
        // ready
        val memberUniqueId = AuthContext.getMemberId()
        val targetDateTime = LocalDateTime.now().minusDays(1)
        val cursorId = 0L
        val size = 10

        every {
            memberNewsCardRetrieve.retrieveNewsCardByMember(
                memberUniqueId = memberUniqueId,
                targetDateTime = targetDateTime,
                cursorId = cursorId,
                size = size
            )
        } returns (
            listOf(
                NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ),
                NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ),
                NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ), NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ), NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ), NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ), NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ), NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ),
                NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                ),
                NewsCard(
                    category = Category(CategoryName.POLITICS),
                    multipleNews = "1, 2, 3, 4, 5",
                    keywords = "테스트 키워드",
                    createdAt = LocalDateTime.now(),
                    modifiedAt = LocalDateTime.now(),
                )
            ))

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/news-card")
                .header("Authorization", "Bearer $memberUniqueId")
                .param("targetDateTime", targetDateTime.toString())
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "숏스 모두 불러오기",
                    HeaderDocumentation.requestHeaders(
                        HeaderDocumentation
                            .headerWithName("Authorization")
                            .description("사용자 식별자 id")
                    ),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("targetDateTime")
                            .description("요청 날짜 및 시간"),
                        RequestDocumentation
                            .parameterWithName("cursorId")
                            .description("커서 아이디"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("페이징 사이즈")
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("API 성공 여부"),
                        fieldWithPath("result[].id").type(JsonFieldType.NUMBER).description("카드뉴스 id"),
                        fieldWithPath("result[].keywords").type(JsonFieldType.STRING).description("키워드"),
                        fieldWithPath("result[].category").type(JsonFieldType.STRING).description("카테고리"),
                        fieldWithPath("result[].crawledDateTime").type(JsonFieldType.STRING).description("크롤링 된 시각"),
                    )
                )
            )
    }

    @Test
    fun 뉴스카드_다_읽었어요() {
        every { memberNewsCardClear.clearMemberNewsCard(any(), any()) } returns (1234)

        // ready
        val url = "/v1/member-news-card"
        val memberId = 1L
        val newsCardId = 1L
        val body = MemberNewsCardClearRequest(memberId, newsCardId)

        // execute
        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .delete(url)
                .content(objectMapper.writeValueAsString(body))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "뉴스카드 다 읽었어요 (오늘 읽을 모든 숏스 삭제)",
                    requestFields(
                        fieldWithPath("memberId").description("멤버 id"),
                        fieldWithPath("newsCardId").description("뉴스카드 id"),
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("status").type(NUMBER).description("API 성공 여부"),
                        fieldWithPath("result.shortsCount").type(NUMBER).description("읽은 숏스 갯수"),
                    )
                )
            )
    }

    @Test
    fun 뉴스카드_삭제() {
        // ready
        val url = "/v1/member-news-card/{newsCardId}"
        val newsCardId = 1L
        val uniqueKey = "uniqueKey"
        val headerName = "Authorization"

        every { memberNewsCardClear.deleteMemberNewsCard(any(), any()) } returns (any())

        // execute
        val response = mockMvc.perform(
            RestDocumentationRequestBuilders.delete(url, newsCardId)
                .header(headerName, uniqueKey)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "오늘의 숏스 단일 삭제",
                    requestHeaders(
                        HeaderDocumentation
                            .headerWithName("Authorization")
                            .description("사용자 식별자 id")
                    ),
                    pathParameters(
                        RequestDocumentation
                            .parameterWithName("newsCardId")
                            .description("뉴스 카드 id"),
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("status").type(NUMBER).description("API 성공 여부"),
                        fieldWithPath("result").type(NULL).description("응답 데이터"),
                    )
                )
            )
    }
}
