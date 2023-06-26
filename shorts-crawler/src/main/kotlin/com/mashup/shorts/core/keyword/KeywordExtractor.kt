package com.mashup.shorts.core.keyword

import java.io.StringReader
import java.util.*
import org.apache.lucene.analysis.ko.KoreanAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.springframework.stereotype.Component
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran

@Component
class KeywordExtractor {

    internal fun extractKeyword(content: String): String {
        val keywordCount = 4
        val analyzer = KoreanAnalyzer()

        val wordFrequencies = mutableMapOf<String, Int>()
        val reader = StringReader(content)
        val tokenStream = analyzer.tokenStream("text", reader)
        val charTermAttribute: CharTermAttribute = tokenStream.addAttribute(CharTermAttribute::class.java)

        tokenStream.reset()
        while (tokenStream.incrementToken()) {
            val term = charTermAttribute.toString()
            if (term !in stopWords && term.length > 1) {
                wordFrequencies[term] = wordFrequencies.getOrDefault(term, 0) + 1
            }
        }
        tokenStream.end()
        tokenStream.close()

        val sortedKeywords = wordFrequencies.entries.sortedByDescending { it.value }
        val topKeywords = sortedKeywords.take(keywordCount).map { it.key }

        return topKeywords.joinToString(", ")
            .replace("[", "")
            .replace("]", "")
    }

    fun extractKeywordV2(content: String): String {
        val keywordCount = 5
        val komoran = Komoran(DEFAULT_MODEL.FULL)
        val nouns = komoran.analyze(content).nouns
        val nounsCountingMap = HashMap<String, Int>()
        val nounsSet = HashSet(nouns)

        nounsSet.map {
            val frequency = Collections.frequency(nouns, it)
            nounsCountingMap[it] = frequency
        }

        val hotKeyword = nounsCountingMap.entries
            .sortedByDescending { it.value }
            .take(keywordCount).map { it.key }

        return hotKeyword.joinToString(", ")
            .replace("[", "")
            .replace("]", "")
    }

}
