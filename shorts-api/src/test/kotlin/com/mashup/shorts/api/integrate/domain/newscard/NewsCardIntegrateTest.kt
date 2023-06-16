package com.mashup.shorts.api.integrate.domain.newscard

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
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
class NewsCardIntegrationTest(
    @Autowired
    protected val mockMvc: MockMvc,
    @Autowired
    protected val objectMapper: ObjectMapper,
) : ApiTestBase() {

    @Test
    @DisplayName("[통합 테스트] : 카드뉴스 내 뉴스 조회 성공 - 첫 페이지 조회 시")
    fun 첫_페이지_조회() {
        // ready
        val url = "/v1/news-card/{newsCardId}"
        val newsCardId = 48L
        val cursorId = 1L
        val size = 10

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/news-card/{newsCardId}", newsCardId)
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        // validate
        val contentAsString = result.response.contentAsString
        assertThat(contentAsString).contains("200")
    }

    @Test
    @DisplayName("[통합 테스트] : 카드뉴스 내 뉴스 조회 예외 - 파라미터 누락 시")
    fun 페이지사이즈_파라미터_누락() {
        // ready
        val url = "/v1/news-card/{newsCardId}"
        val newsCardId = 1L
        val cursorId = 1L
        // val size = 10

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(url, newsCardId)
                .param("cursorId", cursorId.toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        // validate
        val contentAsString = result.response.contentAsString
        assertThat(contentAsString).contains("400")
        assertThat(contentAsString).contains("'size' for method parameter type int is not present")
    }

    @Test
    @DisplayName("[통합 테스트] : 카드뉴스 내 뉴스 조회 예외 - 존재하지 않는 카드뉴스 id")
    fun 카드뉴스ID_허용범위_초과_예외() {
        // ready
        val url = "/v1/news-card/{newsCardId}"
        val newsCardId = 0L
        val cursorId = 0L
        val size = 10

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(url, newsCardId)
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        // validate
        val contentAsString = result.response.contentAsString
        assertThat(contentAsString).contains("404")
    }
}
