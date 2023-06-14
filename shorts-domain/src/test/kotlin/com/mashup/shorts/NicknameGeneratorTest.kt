package com.mashup.shorts

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import com.mashup.shorts.domain.member.Member

class NicknameGeneratorTest {

    @DisplayName("학습 테스트 - 랜덤 닉네임 생성")
    @Test
    fun executeTest() {
        for (i: Int in 0..100) {
            println("nickname = ${Member.generateNickname()}")
        }
    }
}
