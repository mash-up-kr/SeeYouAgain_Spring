package com.mashup.shorts.api.integrate.domain.member.membernewscard

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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.shorts.api.ApiTestBase
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.domain.member.Member

@SpringBootTest
@Disabled
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberNewsCardIntegrationTest(
    @Autowired
    protected val mockMvc: MockMvc,
    @Autowired
    protected val objectMapper: ObjectMapper,
) : ApiTestBase() {

    @Test
    @DisplayName("[통합 테스트] : 카드뉴스_전체_조회")
    fun 카드뉴스_전체_조회() {
        // ready
        val url = "/v1/member-news-card"
        val cursorId = 0L
        val size = 20

        val auth = "Bearer shorts-user"
        AuthContext.USER_CONTEXT.set(
            Member(
                uniqueId = "Unique",
                nickname = "nickname",
                fcmTokenPayload = "payload",
                isAllowedAlarm = true
            )
        )

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header("Authorization", auth)
                .param("targetDateTime", LocalDateTime.now().minusDays(1).minusHours(0).toString())
                .param("cursorId", cursorId.toString())
                .param("size", size.toString())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }

    @Test
    @DisplayName("[통합 테스트] : 뉴스카드 다 읽었어요 성공")
    fun 뉴스카드_다읽었어요() {
        // ready
        val url = "/v1/member-news-card"

        // execute
        val response = mockMvc.perform(
            MockMvcRequestBuilders.delete(url)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        // validate
        response.andExpect { MockMvcResultMatchers.status().isOk() }
        response.andExpect { MockMvcResultMatchers.content().string("shortsCount") }
    }

    @Test
    @DisplayName("[통합 테스트] : 뉴스카드 삭제 성공")
    fun 뉴스카드_삭제() {
        // ready
        val url = "/v1/member-news-card/1"
        val uniqueId = "uniqueId"
        val headerName = "Authorization"

        // execute
        val response = mockMvc.perform(
            MockMvcRequestBuilders.delete(url)
                .header(headerName, uniqueId)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        // validate
        response.andExpect { MockMvcResultMatchers.status().isOk() }
        assertThat(response.andReturn().response.contentAsString.isEmpty()).isTrue
    }
}
