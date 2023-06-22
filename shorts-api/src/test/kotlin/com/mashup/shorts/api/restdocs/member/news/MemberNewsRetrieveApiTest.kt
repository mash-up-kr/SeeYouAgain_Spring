package com.mashup.shorts.api.restdocs.member.news

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.request.RequestDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
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

@WebMvcTest(MemberNewsRetrieveApi::class)
class MemberNewsRetrieveApiTest : ApiDocsTestBase() {
    @MockkBean
    private lateinit var memberNewsRetrieve: MemberNewsRetrieve

    @Test
    fun `오래 간직할 뉴스 모두 조회`() {
        val cursorWrittenDateTime = "2023.06.15. 오후 3:38"
        val size = 10
        val pivot = "ASC"
        every { memberNewsRetrieve.retrieveMemberNews(any(), any(), any(), any()) } returns (
            listOf(
                News(
                    title = "뉴스 제목",
                    content = "뉴스 내용",
                    thumbnailImageUrl = "이미지 링크",
                    newsLink = "뉴스 링크",
                    writtenDateTime = "작성시각",
                    type = "타입",
                    press = "언론사",
                    crawledCount = 1,
                    category = Category(CategoryName.SCIENCE)
                )
            ))

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/member-news")
                .param("cursorWrittenDateTime", cursorWrittenDateTime)
                .param("size", size.toString())
                .param("pivot", pivot)
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "오래 간직할 뉴스 모두 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    RequestDocumentation.queryParameters(
                        RequestDocumentation
                            .parameterWithName("cursorWrittenDateTime")
                            .description("커서 지정 값 ex) 2023.06.15. 오후 3:38 와 같이 입력해 주시고, 첫 페이지 요청 시 빈 문자열을 넣어주세요"),
                        RequestDocumentation
                            .parameterWithName("size")
                            .description("<필수값> 페이징 사이즈(최대 10까지 허용합니다.)"),
                        RequestDocumentation
                            .parameterWithName("pivot")
                            .description("<필수값> 정렬 기준 [ASC, DESC] 둘 중 하나만 허용합니다. ASC는 오래된 순, DESC는 최신 순 입니다."),
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER)
                            .description("API 성공 여부"),
                        PayloadDocumentation.fieldWithPath("result[].id").type(JsonFieldType.NUMBER)
                            .description("뉴스 id"),
                        PayloadDocumentation.fieldWithPath("result[].title").type(JsonFieldType.STRING)
                            .description("뉴스 제목"),
                        PayloadDocumentation.fieldWithPath("result[].thumbnailImageUrl").type(JsonFieldType.STRING)
                            .description("뉴스 이미지 링크"),
                        PayloadDocumentation.fieldWithPath("result[].newsLink").type(JsonFieldType.STRING)
                            .description("뉴스 링크"),
                        PayloadDocumentation.fieldWithPath("result[].press").type(JsonFieldType.STRING)
                            .description("언론사"),
                        PayloadDocumentation.fieldWithPath("result[].writtenDateTime").type(JsonFieldType.STRING)
                            .description("작성 시각"),
                        PayloadDocumentation.fieldWithPath("result[].type").type(JsonFieldType.STRING)
                            .description("헤드라인 뉴스인지, 일반 뉴스인지"),
                    )
                )
            )
    }
}


