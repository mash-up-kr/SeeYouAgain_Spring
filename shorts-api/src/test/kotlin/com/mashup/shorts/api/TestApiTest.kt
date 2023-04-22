package com.mashup.shorts.api

import com.mashup.shorts.Gyun
import com.mashup.shorts.TestController
import com.mashup.shorts.TestService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

/**
 * TestControllerApiTest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 22.
 */
@WebMvcTest(TestController::class)
class TestApiTest : ApiDocsTestBase() {

    @MockkBean
    private lateinit var testService: TestService

    @Test
    fun `테스트 문서를 위한 연습`() {
        every { testService.test() }
                .returns(Gyun("a", "b"))

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