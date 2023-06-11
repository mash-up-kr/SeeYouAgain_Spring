package com.mashup.shorts.core.keyword

import java.io.StringReader
import org.apache.lucene.analysis.ko.KoreanAnalyzer
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute

class KeywordExtractor {

    companion object {
        val stopWords = setOf(
            "은", "는", "이", "가", "을", "를", "과", "와", "에서", "으로", "에게", "으로부터", "에", "의"
        )
    }

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
}
