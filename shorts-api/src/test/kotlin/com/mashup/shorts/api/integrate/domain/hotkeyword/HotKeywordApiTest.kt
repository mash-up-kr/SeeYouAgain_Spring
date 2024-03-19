package com.mashup.shorts.api.integrate.domain.hotkeyword

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.shorts.api.ApiTestBase

@SpringBootTest
@Disabled
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class HotKeywordApiTestIntegrationTest(
    @Autowired
    protected val mockMvc: MockMvc,
    @Autowired
    protected val objectMapper: ObjectMapper,
) : ApiTestBase() {

    @Test
    @DisplayName("[통합 테스트] : 키워드로부터 숏스 조회")
    fun `키워드로부터 숏스 조회`() {
        // ready
        val url = "/v1/hot-keywords/{keyword}"
        val keyword = "감사"

        // execute
        mockMvc.perform(
            MockMvcRequestBuilders.get(url, keyword)
                .param("cursorId", "0")
                .param("size", "10")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }
}
