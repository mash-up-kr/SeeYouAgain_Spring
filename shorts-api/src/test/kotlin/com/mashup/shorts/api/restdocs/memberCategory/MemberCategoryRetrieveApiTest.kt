package com.mashup.shorts.api.restdocs.memberCategory

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.membercategory.MemberCategoryRetrieve
import com.mashup.shorts.domain.home.memberCategory.MemberCategoryRetrieveApi
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

/**
 * MemberCategoryRetrieveApiTest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 20.
 */
@WebMvcTest(MemberCategoryRetrieveApi::class)
class MemberCategoryRetrieveApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberCategoryRetrieve: MemberCategoryRetrieve

    @Test
    fun `멤버 관심 카테고리 조회`() {
        every { memberCategoryRetrieve.retrieveMemberCategory(any()) } returns(listOf(CategoryName.CULTURE, CategoryName.ECONOMIC))

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/v1/member/category")
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "멤버 관심 카테고리 조회",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("API HTTP Status 값"),
                        PayloadDocumentation.fieldWithPath("result.categories").type(JsonFieldType.ARRAY).description("해당 멤버의 관심 카테고리 리스트")
                    )
                )
            )
    }
}
