package com.mashup.shorts.api.integrate.domain.newscard

import java.time.LocalDateTime
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.shorts.api.ApiTestBase

@SpringBootTest
@Disabled
@ActiveProfiles("test")
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
        val url = "/v1/news-card"
        val newsCardId = 1L
        val cursorId = 1L
        val size = 10

        // execute
        mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .param("newsCardId", newsCardId.toString())
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())
    }

    @Test
    @DisplayName("[통합 테스트] : 카드뉴스 내 뉴스 조회 예외 - 파라미터 누락 시")
    fun 페이지사이즈_파라미터_누락() {
        // ready
        val url = "/v1/news-card"
        val newsCardId = 1L
        val cursorId = 1L
        // val size = 10

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .param("newsCardId", newsCardId.toString())
                .param("cursorId", cursorId.toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        // validate
        val contentAsString = result.response.contentAsString
        assertThat(contentAsString).contains("400")
        assertThat(contentAsString).contains("is missing")
    }

    @Test
    @DisplayName("[통합 테스트] : 카드뉴스 내 뉴스 조회 예외 - 타입 불일치")
    fun 페이지사이즈_파라미터_타입_불일치() {
        // ready
        val url = "/v1/news-card"
        val newsCardId = 1L
        val cursorId = 1L
        val size = "size"

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .param("newsCardId", newsCardId.toString())
                .param("cursorId", cursorId.toString())
                .param("size", size)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()

        // validate
        val contentAsString = result.response.contentAsString
        assertThat(contentAsString).contains("400")
        assertThat(contentAsString).contains("not matched")
    }

    @Test
    @DisplayName("[통합 테스트] : 카드뉴스 내 뉴스 조회 예외 - 존재하지 않는 카드뉴스 id")
    fun 카드뉴스ID_허용범위_초과_예외() {
        // ready
        val url = "/v1/news-card"
        val newsCardId = 0L
        val cursorId = 0L
        val size = 10

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .param("newsCardId", newsCardId.toString())
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
        // assertThat(contentAsString).contains("해당 뉴스 카드는 존재하지 않습니다.")
    }

    @Test
    @DisplayName("[통합 테스트] : 카드뉴스_전체_조회")
    fun 카드뉴스_전체_조회() {
        // ready
        val url = "/v1/news-card"
        val cursorId = 0L
        val size = 20

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header("Authorization", "Bearer 1234")
                .param("targetDateTime", LocalDateTime.now().minusDays(1).minusHours(0).toString())
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }
}
