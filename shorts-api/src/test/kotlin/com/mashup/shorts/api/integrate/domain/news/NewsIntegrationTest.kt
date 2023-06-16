package com.mashup.shorts.api.integrate.domain.news

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.shorts.api.ApiTestBase

@SpringBootTest
@Disabled
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NewsIntegrationTest(
    @Autowired
    protected val mockMvc: MockMvc,
    @Autowired
    protected val objectMapper: ObjectMapper,
) : ApiTestBase() {

    @Test
    @DisplayName("[통합 테스트] : 뉴스 자세히 보기 테스트 - 성공")
    fun 뉴스_자세히보기_링크() {
        // ready
        val url = "/v1/news/{newsId}"
        val newsId = 500L

        // execute
        val response = mockMvc.perform(
            MockMvcRequestBuilders.get(url, newsId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        // validate
        response.andExpect { MockMvcResultMatchers.status().isOk() }
        response.andExpect { jsonPath("$.status").value(200) }
    }
}
