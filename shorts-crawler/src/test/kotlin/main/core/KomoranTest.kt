package main.core

import java.io.StringReader
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.junit.jupiter.api.Test
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran

class KomoranTest {

    @Test
    fun main() {
        val komoran = Komoran(DEFAULT_MODEL.LIGHT)
        val document = "野 \"日, '들러리 시찰' 후 수산물 수입 요구…짜고치는 고스톱\""
        val analyzeResultList = komoran.analyze(document)
        val tokenList = analyzeResultList.tokenList
        val nouns = analyzeResultList.nouns

        for (noun in nouns) {
            println("noun = ${noun}")
        }
    }

    @Test
    fun main2() {
        val sentence = "野 \"日, '들러리 시찰' 후 수산물 수입 요구…짜고치는 고스톱\""

        // Lucene의 StandardAnalyzer를 사용하여 단어의 등장 횟수 계산
        val analyzer: Analyzer = StandardAnalyzer()

        // 문장을 단어로 분석하여 단어의 등장 횟수 계산
        val wordCountMap = HashMap<String, Int>()

        StringReader(sentence).use { reader ->
            val tokenStream = analyzer.tokenStream("field", reader)
            val termAttribute = tokenStream.addAttribute(CharTermAttribute::class.java)
            tokenStream.reset()

            while (tokenStream.incrementToken()) {
                val word = termAttribute.toString()
                val count = wordCountMap.getOrDefault(word, 0)
                wordCountMap[word] = count + 1
            }

            tokenStream.end()
        }

        // 등장 횟수에 따라 단어를 내림차순으로 정렬하여 출력
        wordCountMap.entries
            .sortedByDescending { it.value }
            .forEach { entry ->
                val word = entry.key
                val count = entry.value
                println("$word ($count)")
            }
    }
}
