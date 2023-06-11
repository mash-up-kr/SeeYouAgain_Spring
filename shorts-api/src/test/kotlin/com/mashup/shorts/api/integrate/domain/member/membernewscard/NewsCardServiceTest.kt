package com.mashup.shorts.api.integrate.domain.member.membernewscard

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
import com.mashup.shorts.domain.member.membernewscard.dto.MemberNewsCardClearRequest

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
    @DisplayName("[통합 테스트] : 뉴스 다 읽었어요 성공 - MemberNewsCard 테이블에서 제거")
    fun 뉴스_다읽었어요() {
        // ready
        val url = "/v1/member-news-card"
        val memberId = 1L
        val newsCardId = 1L
        val body = MemberNewsCardClearRequest(memberId, newsCardId)

        // execute
        val response = mockMvc.perform(
            MockMvcRequestBuilders.delete(url)
                .content(objectMapper.writeValueAsString(body))
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
        ).andDo(MockMvcResultHandlers.print())

        // validate
        response.andExpect { MockMvcResultMatchers.status().isOk() }
        response.andExpect { MockMvcResultMatchers.content().string("shortsCount") }
    }
}
