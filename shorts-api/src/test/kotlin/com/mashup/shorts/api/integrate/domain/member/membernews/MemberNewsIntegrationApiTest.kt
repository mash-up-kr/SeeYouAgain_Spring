package com.mashup.shorts.api.integrate.domain.member.membernews

import java.time.LocalDateTime
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.transaction.annotation.Transactional
import com.fasterxml.jackson.databind.ObjectMapper
import com.mashup.shorts.api.ApiTestBase
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.newscard.Pivots

@SpringBootTest
@Disabled
@ActiveProfiles("test")
@Transactional
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberNewsIntegrationApiTest(
    @Autowired
    protected val mockMvc: MockMvc,
    @Autowired
    protected val objectMapper: ObjectMapper,
) : ApiTestBase() {

    @Test
    @DisplayName("[통합 테스트] : 멤버 오래 간직할 뉴스 모두 조회")
    fun `오래 간직할 뉴스 모두 조회`() {
        // ready
        val url = "/v1/member-news"
        val size = 10

        val auth = "Bearer shorts-user"
        AuthContext.USER_CONTEXT.set(Member(uniqueId = "unique", nickname = "nickname"))

        // execute
        val result = mockMvc.perform(
            MockMvcRequestBuilders.get(url)
                .header("Authorization", auth)
                .param("cursorWrittenDateTime", LocalDateTime.now().minusDays(1).minusHours(0).toString())
                .param("size", size.toString())
                .param("pivot", Pivots.ASC.name)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
    }
}

