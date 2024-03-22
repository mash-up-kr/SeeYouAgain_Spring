package com.mashup.shorts.core.legacy

import java.io.StringReader
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.ko.KoreanTokenizer
import org.apache.lucene.analysis.ko.KoreanTokenizer.DecompoundMode
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import com.mashup.shorts.KeywordExtractor
import com.mashup.shorts.consts.stopWords
import com.mashup.shorts.consts.CONTENT_WEIGHT
import com.mashup.shorts.consts.TITLE_WEIGHT

@Component
@Qualifier("LuceneAnalyzerKeywordExtractor")
class LuceneAnalyzerKeywordExtractor : KeywordExtractor {

    override fun extractKeyword(title: String, content: String): String {
        val wordFrequencies = calculateWordFrequencies(title, content)
        return formatResult(wordFrequencies)
    }

    private fun calculateWordFrequencies(title: String, content: String): MutableMap<String, Int> {
        val titleFrequencies = calculateFrequency(title, TITLE_WEIGHT)
        val contentFrequencies = calculateFrequency(content, CONTENT_WEIGHT)

        return mergeFrequencies(titleFrequencies, contentFrequencies)
    }

    private fun mergeFrequencies(
        titleFrequencies: Map<String, Int>,
        contentFrequencies: Map<String, Int>,
    ): MutableMap<String, Int> {
        val wordFrequencies = titleFrequencies.toMutableMap()

        contentFrequencies.forEach { (key, value) ->
            wordFrequencies[key] = wordFrequencies.getOrDefault(key, DEFAULT_FREQUENCY) + value
        }

        return wordFrequencies
    }

    private fun calculateFrequency(text: String, weight: Double): Map<String, Int> {
        val wordFrequencies = mutableMapOf<String, Int>()
        val tokens = text.split(" ")

        tokens.forEach { token ->
            val term = createTokenStream(token).use { stream ->
                stream.reset()
                val termAttribute = stream.getAttribute(CharTermAttribute::class.java)
                val result = mutableListOf<String>()
                while (stream.incrementToken()) {
                    val term = termAttribute.toString()
                    if (isValidTerm(term)) {
                        result.add(term)
                    }
                }
                stream.end()
                result.joinToString(" ")
            }

            if (term.isNotBlank()) {
                val frequency = wordFrequencies.getOrDefault(term, DEFAULT_FREQUENCY)
                wordFrequencies[term] = frequency + weight.toInt()
            }
        }

        return wordFrequencies
    }

    private fun isValidTerm(term: String): Boolean {
        return term !in stopWords && term.length > 1
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
