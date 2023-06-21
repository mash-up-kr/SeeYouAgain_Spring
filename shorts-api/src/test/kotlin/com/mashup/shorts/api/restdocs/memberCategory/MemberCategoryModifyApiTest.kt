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
import com.mashup.shorts.domain.membercategory.MemberCategoryCreate
import com.mashup.shorts.domain.home.memberCategory.CategoryCreateBulkRequest
import com.mashup.shorts.domain.home.memberCategory.MemberCategoryCreateApi
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

/**
 * MemberCategoryModifyApiTest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 18.
 */

@WebMvcTest(MemberCategoryCreateApi::class)
class MemberCategoryModifyApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberCategoryCreate: MemberCategoryCreate

    @Test
    fun `멤버 관심 카테고리 수정`() {
        every { memberCategoryCreate.modifyMemberCategory(any(), any()) } returns(Unit)

        val requestBody = CategoryCreateBulkRequest(
            categoryNames = listOf(CategoryName.CULTURE, CategoryName.ECONOMIC)
        )

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .put("/v1/member/category")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                MockMvcRestDocumentation.document(
                    "멤버 관심 카테고리 수정",
                    RestDocsUtils.getDocumentRequest(),
                    RestDocsUtils.getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    PayloadDocumentation.requestFields(
                        PayloadDocumentation.fieldWithPath("categoryNames").type(JsonFieldType.ARRAY).description("사용자가 수정할 카테고리 리스트 (ex: [POLITICS, ECONOMIC, SOCIETY, CULTURE, WORLD, SCIENCE]")
                    ),
                    PayloadDocumentation.responseFields(
                        PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.NUMBER).description("API HTTP Status 값")
                    )
                )
            )
    }
}
