package com.mashup.shorts.api.restdocs.keyword

import java.time.LocalDateTime
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
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
import com.mashup.shorts.domain.keyword.dtomapper.RetrieveDetailHotKeyWordResponseMapper
import com.mashup.shorts.domain.newscard.NewsCard
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

@WebMvcTest(HotKeywordsRetrieveApi::class)
class HotKeywordsRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var hotKeywordRetrieve: HotKeywordRetrieve

    @Test
    fun `핫 키워드 조회`() {
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
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.createdAt").type(JsonFieldType.STRING)
                            .description("핫 키워드 생성 시간"),
                        PayloadDocumentation.fieldWithPath("result.ranking").type(JsonFieldType.ARRAY)
                            .description("핫 키워드 순위 (1위 ~ 10위)")
                    )
                )
            )
    }

    @Test
    fun `핫 키워드로 숏스 조회`() {
        every { hotKeywordRetrieve.retrieveDetailHotKeyword(any(), any(), any()) } returns (
            RetrieveDetailHotKeyWordResponseMapper.persistenceToResponseForm(
                listOf(
                    NewsCard(
                        category = Category(CategoryName.CULTURE),
                        multipleNews = "1, 2, 3, 4, 5",
                        keywords = "스프링, 빠지, 가평, 디스코드",
                        createdAt = LocalDateTime.now(),
                        modifiedAt = LocalDateTime.now(),
                    ),
                    NewsCard(
                        category = Category(CategoryName.SOCIETY),
                        multipleNews = "6, 7, 8, 9, 10",
                        keywords = "에어컨, 선풍기, 무더위, 장마",
                        createdAt = LocalDateTime.now(),
                        modifiedAt = LocalDateTime.now(),
                    )
                )
            )
            )

        mockMvc.perform(
            RestDocumentationRequestBuilders.get("/v1/hot-keywords/{keyword}", "위원")
                .param("cursorId", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "핫 키워드로 숏스 조회",
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
                            .parameterWithName("cursorId")
                            .description("커서 아이디(기본 값은 0으로 지정됩니다.)"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 10까지 허용합니다.)"),
                    ),
                    responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result[].id").type(JsonFieldType.NUMBER)
                            .description("숏스 id"),
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
