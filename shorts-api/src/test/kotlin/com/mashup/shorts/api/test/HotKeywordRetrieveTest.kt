package com.mashup.shorts.api.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import com.mashup.shorts.domain.keyword.HotKeyword
import com.mashup.shorts.domain.keyword.KeywordRanking

@ActiveProfiles("test")
@SpringBootTest
class HotKeywordRetrieveTest {

    @Test
    fun test() {
        val hotKeyword = HotKeyword("(대표 자격, 4.5), (대통령 명령, 4.0), (감소, 4.0), (비리 혐의, 3.0), (콘텐츠 생성, 3.0), (판매, 3.0), (국내, 3.0), (카카오, 3.0), (오늘, 2.5), (경기, 2.5),")
        val regex = "\\((.*?), (\\d+)\\)".toRegex()
        val matches = regex.findAll(hotKeyword.keywordRanking)


        val result = mutableListOf<String>()
        for (match in matches) {
            result.add(match.groupValues[1])
        }
        val keywordRanking = KeywordRanking(hotKeyword.createdAt.toString(), result)
        val keywordRanking1 = KeywordRanking(hotKeyword.createdAt.toString(), result)
        println("keywordRanking1 = ${keywordRanking1}")
    }
}
