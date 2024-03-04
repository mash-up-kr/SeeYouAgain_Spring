package com.mashup.shorts.core.keywordextractor

import java.io.StringReader
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.ko.KoreanTokenizer
import org.apache.lucene.analysis.ko.KoreanTokenizer.DecompoundMode
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component

@Component
@Qualifier("LuceneAnalyzerKeywordExtractor")
class LuceneAnalyzerKeywordExtractor : KeywordExtractor {

    override fun extractKeyword(title: String, content: String): String {
        val titleFrequencies = calculateWordFrequencies(title, TITLE_WEIGHT)
        val contentFrequencies = calculateWordFrequencies(content, CONTENT_WEIGHT)
        val wordFrequencies = titleFrequencies.toMutableMap()

        contentFrequencies.forEach { (key, value) ->
            wordFrequencies[key] = wordFrequencies.getOrDefault(key, DEFAULT_FREQUENCY) + value
        }

        return formatResult(wordFrequencies)
    }

    private fun calculateWordFrequencies(text: String, weight: Double): Map<String, Int> {
        val wordFrequencies = mutableMapOf<String, Int>()
        val tokenStream = createTokenStream(text)
        tokenStream.use { token ->
            token.reset()
            while (token.incrementToken()) {
                val term = token.getAttribute(CharTermAttribute::class.java).toString()
                if (term !in stopWords && term.length > 1) {
                    val frequency = wordFrequencies.getOrDefault(term, DEFAULT_FREQUENCY)
                    wordFrequencies[term] = frequency + weight.toInt()
                }
            }
            token.end()
        }
        return wordFrequencies
    }

    private fun createTokenStream(text: String): TokenStream {
        val reader = StringReader(text)
        return luceneKoreanAnalyzer.tokenStream(TOKEN_STREAM_FIELD_NAME_TYPE, reader)
    }

    private fun formatResult(wordFrequencies: Map<String, Int>): String {
        val sortedKeywords = wordFrequencies.entries.sortedByDescending { it.value }
        val topKeywords = sortedKeywords.take(KEYWORD_COUNT).map { it.key }
        return topKeywords.joinToString(", ")
    }

    companion object {
        private const val KEYWORD_COUNT = 5
        private const val TITLE_WEIGHT = 1.5
        private const val CONTENT_WEIGHT = 1.0
        private const val DEFAULT_FREQUENCY = 0
        private const val TOKEN_STREAM_FIELD_NAME_TYPE = "text"

        private val luceneKoreanAnalyzer = object : Analyzer() {
            override fun createComponents(fieldName: String?): TokenStreamComponents {
                val koreanTokenizer = KoreanTokenizer(
                    KoreanTokenizer.DEFAULT_TOKEN_ATTRIBUTE_FACTORY,
                    null,
                    DecompoundMode.NONE,
                    true
                )
                return TokenStreamComponents(koreanTokenizer)
            }
        }
    }
}
