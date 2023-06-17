package com.mashup.shorts.api.restdocs.member.newscard

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.headers.HeaderDocumentation
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardRetrieve
import com.mashup.shorts.domain.member.membernewscard.MemberNewsCardRetrieveApi
import com.mashup.shorts.domain.member.membernewscard.dtomapper.RetrieveAllNewsCardResponseMapper
import com.mashup.shorts.domain.newscard.NewsCard
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(MemberNewsCardRetrieveApi::class)
class MemberNewsCardRetrieveTest : ApiDocsTestBase() {

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
            RetrieveAllNewsCardResponseMapper.persistenceToResponseForm(
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
            )

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/member-news-card")
                .header("Authorization", "Bearer $memberUniqueId")
                .param("targetDateTime", targetDateTime.toString())
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
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
                            .description("커서 아이디(기본 값은 0으로 지정됩니다.)"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 10까지 허용합니다.)"),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        PayloadDocumentation.fieldWithPath("result[].id").type(JsonFieldType.NUMBER)
                            .description("카드뉴스 id"),
                        PayloadDocumentation.fieldWithPath("result[].keywords").type(JsonFieldType.STRING)
                            .description("키워드"),
                        PayloadDocumentation.fieldWithPath("result[].category").type(JsonFieldType.STRING)
                            .description("카테고리"),
                        PayloadDocumentation.fieldWithPath("result[].crawledDateTime").type(JsonFieldType.STRING)
                            .description("크롤링 된 시각"),
                    )
                )
            )
    }
}
