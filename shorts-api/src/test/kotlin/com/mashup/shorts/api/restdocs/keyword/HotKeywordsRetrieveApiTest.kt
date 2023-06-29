package com.mashup.shorts.api.restdocs.keyword

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.request.RequestDocumentation.queryParameters
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.category.Category
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.hot.keyword.HotKeywordsRetrieveApi
import com.mashup.shorts.domain.keyword.HotKeywordRetrieve
import com.mashup.shorts.domain.keyword.KeywordRanking
import com.mashup.shorts.domain.news.News
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(HotKeywordsRetrieveApi::class)
class HotKeywordsRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var hotKeywordRetrieve: HotKeywordRetrieve

    @Test
    fun `핫 키워드 조회`() {
        every { hotKeywordRetrieve.retrieveHotKeywords(any()) } returns (KeywordRanking(
            createdAt = "2023-06-25 11:00",
            ranking = listOf("키워드1", "키워드2", "키워드3")
        ))

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/hot-keywords")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "핫 키워드 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                        fieldWithPath("result.createdAt").type(JsonFieldType.STRING)
                            .description("핫 키워드 생성 시간"),
                        fieldWithPath("result.ranking").type(JsonFieldType.ARRAY)
                            .description("핫 키워드 순위 (1위 ~ 10위)")
                    )
                )
            )
    }

    @Test
    fun `핫 키워드로 뉴스 조회`() {
        every {
            hotKeywordRetrieve.retrieveDetailHotKeyword(
                any(), any(), any(), any()
            )
        } returns (
            listOf(
                News(
                    title = "Title~!",
                    content = "Contents ",
                    thumbnailImageUrl = "IMAGE LINK",
                    newsLink = "NEWS LINK",
                    press = "TYN",
                    writtenDateTime = "TODAY",
                    type = "HEADLINE",
                    crawledCount = 1,
                    category = Category(CategoryName.CULTURE)
                ),
                News(
                    title = "Title~!",
                    content = "Contents ",
                    thumbnailImageUrl = "IMAGE LINK",
                    newsLink = "NEWS LINK",
                    press = "TYN",
                    writtenDateTime = "TODAY",
                    type = "HEADLINE",
                    crawledCount = 1,
                    category = Category(CategoryName.CULTURE)
                ),
            )
        )

        mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/hot-keywords/{keyword}", "위원")
                .param("targetDateTime", LocalDateTime.now().toString())
                .param("cursorId", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "핫 키워드로 뉴스 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    pathParameters(
                        RequestDocumentation
                            .parameterWithName("keyword")
                            .description("<필수값> 키워드"),
                    ),
                    queryParameters(
                        RequestDocumentation
                            .parameterWithName("targetDateTime")
                            .description("타입 : LocalDateTime, 조회할 날짜/시간"),
                        RequestDocumentation
                            .parameterWithName("cursorId")
                            .description("커서 아이디(기본 값은 0으로 지정됩니다.)"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 20까지 허용합니다.)"),
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        fieldWithPath("result[].id").type(JsonFieldType.NUMBER)
                            .description("뉴스 id"),
                        fieldWithPath("result[].title").type(JsonFieldType.STRING)
                            .description("뉴스 제목"),
                        fieldWithPath("result[].thumbnailImageUrl").type(JsonFieldType.STRING)
                            .description("뉴스 이미지 링크"),
                        fieldWithPath("result[].newsLink").type(JsonFieldType.STRING)
                            .description("뉴스 링크"),
                        fieldWithPath("result[].press").type(JsonFieldType.STRING)
                            .description("언론사"),
                        fieldWithPath("result[].writtenDateTime").type(JsonFieldType.STRING)
                            .description("작성 시각"),
                        fieldWithPath("result[].type").type(JsonFieldType.STRING)
                            .description("헤드라인 뉴스인지, 일반 뉴스인지"),
                    )
                )
            )
    }
}
