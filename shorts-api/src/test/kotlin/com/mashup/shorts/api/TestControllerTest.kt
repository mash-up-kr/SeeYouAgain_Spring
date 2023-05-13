package com.mashup.shorts.api

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import com.mashup.shorts.TestController

/**
 * TestControllerTest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 14.
 */
@WebMvcTest(TestController::class)
class TestControllerTest : ApiDocsTestBase() {

    @Test
    fun `테스트 문서를 위한 연습`() {

        val result = mockMvc.perform(
            RestDocumentationRequestBuilders
                .get("/test")
                .contentType(MediaType.APPLICATION_JSON)
        )

        result.andExpect(MockMvcResultMatchers.status().isOk)
            .andDo(
                document(
                    "Gyunny",
                    /*requestHeaders(
                        HeaderDocumentation.headerWithName("test").description("test")
                    ),*/
//                    RequestDocumentation.pathParameters(
//                            RequestDocumentation.parameterWithName("id").description("id path"),
//                    ),
                    PayloadDocumentation.responseFields(
                        //PayloadDocumentation.fieldWithPath("status").type(JsonFieldType.STRING).description("API 성공 여부"),
                        PayloadDocumentation.fieldWithPath("a").type(JsonFieldType.STRING).description("설명 a"),
                        PayloadDocumentation.fieldWithPath("b").type(JsonFieldType.STRING).description("설명 b"),
                    )
                )
            )
    }
}
