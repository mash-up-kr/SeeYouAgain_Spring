package com.mashup.shorts.api.restdocs.member.membercategory

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.requestFields
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.api.ApiDocsTestBase
import com.mashup.shorts.api.restdocs.util.PageHeaderSnippet
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentRequest
import com.mashup.shorts.api.restdocs.util.RestDocsUtils.getDocumentResponse
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.home.membercategory.MemberCategoryCreateApi
import com.mashup.shorts.domain.home.membercategory.dto.CategoryCreateBulkRequest
import com.mashup.shorts.domain.membercategory.MemberCategoryCreate
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every

/**
 * MemberCategoryCreateApiTest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 11.
 */
@WebMvcTest(MemberCategoryCreateApi::class)
class MemberCategoryCreateApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var memberCategoryCreate: MemberCategoryCreate

    @Test
    fun `멤버 관심 카테고리 설정`() {
        every { memberCategoryCreate.createCategory(any(), any(), any()) } returns (Unit)

        val requestBody = CategoryCreateBulkRequest(
            categoryNames = listOf(CategoryName.CULTURE, CategoryName.ECONOMIC),
            fcmTokenPayload = "payload"
        )

        val response = mockMvc.perform(
            RestDocumentationRequestBuilders
                .post("/v1/member/category")
                .content(objectMapper.writeValueAsString(requestBody))
                .contentType(MediaType.APPLICATION_JSON)
        )

        response.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "멤버 관심 카테고리 설정",
                    getDocumentRequest(),
                    getDocumentResponse(),
                    PageHeaderSnippet.pageHeaderSnippet(),
                    requestFields(
                        fieldWithPath("categoryNames")
                            .type(JsonFieldType.ARRAY)
                            .description("사용자가 선택한 카테고리 리스트 (ex: [POLITICS, ECONOMIC, SOCIETY, CULTURE, WORLD, SCIENCE]"),
                        fieldWithPath("fcmTokenPayload")
                            .type(JsonFieldType.STRING)
                            .description("각 디바이스의 FCM Token Payload를 넣어주세요.")
                    ),
                    PayloadDocumentation.responseFields(
                        fieldWithPath("status").type(JsonFieldType.NUMBER).description("API HTTP Status 값"),
                        fieldWithPath("result.uniqueId").type(JsonFieldType.STRING).description("유저의 고유한 아이디"),
                    )
                )
            )
    }
}
