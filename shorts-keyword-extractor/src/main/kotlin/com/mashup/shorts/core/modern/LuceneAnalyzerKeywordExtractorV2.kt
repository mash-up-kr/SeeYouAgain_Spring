package com.mashup.shorts.core.modern

import java.io.StringReader
import kotlin.math.ln
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.ko.KoreanTokenizer
import org.apache.lucene.analysis.ko.KoreanTokenizer.DecompoundMode
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component
import com.mashup.shorts.KeywordExtractor
import com.mashup.shorts.consts.stopWords

@Primary
@Component
class LuceneAnalyzerKeywordExtractorV2 : KeywordExtractor {

    /* 변수 `k1`에 대한 부연 설명 By K-Diger
    키워드가 문서에 얼마나 자주 등장하더라도 점수에 주는 영향을 제한하는 가중치이다.
    일반적으로 1.2 ~ 2.0 사이의 값을 사용하고

    - k1 값이 높을수록 키워드가 한 번 더 등장했을 때, 이전까지에 비해 점수를 얼마나 더 높여주어야 하는지를 결정한다.
    - k1 값이 낮을수록 키워드가 여러 번 등장해도 점수에 큰 영향을 주지 않는다.
    */
    private val k1 = 1.2

    /* 변수 `b`에 대한 부연 설명 By K-Diger
    문서 길이(document length)에 대한 중요도를 제어한다.
    문서가 길수록 패널티를 부여하여 길이에 상관없이 키워드의 중요도를 평가하도록한다.
    일반적으로 0.75 ~ 1.0 사이의 값을 사용하고.

    - b 값이 높을수록 문서 길이가 길수록 패널티를 더 크게 부여한다.
    - b 값이 낮을수록 문서 길이를 무시하고 키워드의 출현 빈도만 고려한다.
    */
    private val b = 0.75

    override fun extractKeyword(title: String, content: String): String {
        val wordFrequencies = calculateWordFrequencies(title, content)
        return formatResult(wordFrequencies)
    }

    private fun calculateWordFrequencies(title: String, content: String): MutableMap<String, Double> {
        val titleLength = title.length.toDouble()
        val contentLength = content.length.toDouble()
        val avgDocLength = titleLength + contentLength

        val titleFrequencies = calculateFrequency(title, titleLength, avgDocLength)
        val contentFrequencies = calculateFrequency(content, contentLength, avgDocLength)

        return mergeFrequencies(titleFrequencies, contentFrequencies)
    }

    private fun calculateFrequency(text: String, docLength: Double, avgDocLength: Double): Map<String, Double> {
        val tokens = text.split(" ")
        val wordScores = mutableMapOf<String, Double>()

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
                val termFreq = wordScores.getOrDefault(term, 0.0) + 1
                wordScores[term] = termFreq
            }
        }

        val wordBM25Scores = mutableMapOf<String, Double>()
        wordScores.forEach { (term, freq) ->
            val idf = calculateInverseDocumentFrequency(1, 2)
            val bm25Score = idf * ((freq * (k1 + 1)) / (freq + k1 * (1 - b + b * (docLength / avgDocLength))))
            wordBM25Scores[term] = bm25Score
        }

        return wordBM25Scores
    }

    private fun calculateInverseDocumentFrequency(docFreq: Int, totalDocs: Int): Double {
        // 문서 빈도수가 0인 경우, 혹은 전체 문서 수가 0인 경우 IDF 값은 0으로 처리한다. By K-Diger
        if (docFreq == 0 || totalDocs == 0) {
            return 0.0
        }

        // IDF 계산 시 안정화를 위해 1을 더하고, 로그의 결과에 1을 더하여 IDF 값의 범위를 조정한다. By K-Diger
        return ln((totalDocs.toDouble() + 1) / (docFreq + 1)) + 1
    }

    private fun mergeFrequencies(
        titleFrequencies: Map<String, Double>,
        contentFrequencies: Map<String, Double>,
    ): MutableMap<String, Double> {
        val wordFrequencies = titleFrequencies.toMutableMap()

        contentFrequencies.forEach { (key, value) ->
            wordFrequencies[key] = wordFrequencies.getOrDefault(key, 0.0) + value
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

    private fun formatResult(wordFrequencies: MutableMap<String, Double>): String {
        val sortedKeywords = wordFrequencies.entries.sortedByDescending { it.value }
        val topKeywords = sortedKeywords.take(KEYWORD_COUNT).map { it.key }
        return topKeywords.joinToString(", ")
    }

    companion object {
        private const val KEYWORD_COUNT = 5
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
