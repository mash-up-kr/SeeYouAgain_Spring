package com.mashup.shorts.core

import java.io.StringReader
import kotlin.math.ln
import org.apache.lucene.analysis.Analyzer
import org.apache.lucene.analysis.TokenStream
import org.apache.lucene.analysis.ko.KoreanTokenizer
import org.apache.lucene.analysis.ko.KoreanTokenizer.DecompoundMode
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute
import org.junit.jupiter.api.Test
import com.mashup.shorts.consts.stopWords


class KeywordExtractorV2Test {

    @Test
    fun main() {
        val title = "리스크관리 경고등 켜진 저축은행·상호금융, 적자심화·연체율 증가…\"PF로 대손충당금 확대\""
        val content =
            "[디지털데일리 박기록기자] 국내 서민금융을 대표하는 저축은행업계와 상호금융업계의 적자가 심화되고 있으며, 특히 저축은행업계의 기업 대출금 연체율이 전년대비 크게 증가하는 등 리스크관리에 비상등이 켜졌다. 금융 감독당국은 그러나 \"저축은행의 적자 규모 확대는 PF(프로젝트파이낸싱) 대출 예상 손실 가능성에 대비해 선제적으로 충당금을 적립한 데 주로 기인하고, 상호금융조합은 경제사업 부문의 손실이 확대되면서 순이익이 감소한 측면이 있다\"고 분석했다. 또한 저축은행 연체율의 경우, 과거 위기시에 비해서는 여전히 낮은 수준이라고 진단했다. 22일 금융감독원에 따르면, 먼저 저축은행업계의 2023년말 기준 총자산은 126.6조원으로 전년말(138.6조원) 대비 12.0조원(↓8.7%) 감소했다. 이는 고금리 상황의 지속, 경기 회복 지연 등으로 주로 기업대출 위주로 대출자산이 전년대비 11.0조원 감소한 데 주로 기인했다. 2023년말 기준, 저축은행업계의 당기순이익(손실)은 -5559억원으로 전년(+1조5622억원) 대비 적자 전환했다. 이는 조달비용 증가 등에 따른 이자손익 감소(-1.3조원), PF대출 관련 선제적 대손충당금 적립 등으로 인한 대손비용 증가(+1.3조원)에 기인했다는 분석이다. 특히 부동산 PF대출 미래 예상 손실 등에 대비한 충당금 추가 적립(+0.4조원)으로 작년 4분기 적자폭이 크게 확대됐다는 설명이다. ⓒ금융감독원 이와함께 작년말 기준, 저축은행업계의 연체율은 6.55%를 기록해 년말(3.41%) 대비 +3.14%p나 급상승한 것으로 나타났다. 특히 연체율의 경우, 가계대출 연체율은 5.01%로 전년말(4.74%) 대비 +0.27%p 증가한 반면, 기업대출은 연체율은 8.02%로 전년말(2.90%) 대비 +5.12%p나 상승해 주로 기업대출 연체율 관리에 적색 경고등이 들어온 것으로 분석됐다. 관련하여 작년말 기준, 저축은행업계의 고정이하여신비율은 7.72%로 역시 전년말(4.08%) 대비 +3.64%p 상승한 것으로 나타났다. 다만 금감원은 감독규정상 요적립액 대비 충당금적립률은 113.9%로 전년말 대비 +0.5%p 상승하는 등 모든 저축은행이 규제비율(100%)을 상회해 안전장치를 강화했다는 설명이다. 한편 신협, 농협 등 상호금융업계의 경우, 작년말 기준 총자산은 726.5조원으로 전년말(687.9조원) 대비 +38.6조원(↑5.6%) 증가했다. 총여신은 510.4조원으로 전년(498.3조원) 대비 +12.1조원(↑2.4%) 증가했다. 다만 이 중 가계대출은 21.3조원 감소(↓8.8%)한 반면, 기업대출이 +31.7조원(↑13.3%) 증가했다. 같은기간 당기순이익은 2조 407억원으로 전년(3조 1276억원) 대비 1조 869억원이나 감소(34.8%↓)했다. 이 중 신용사업부문(금융) 순이익(5조 6669억원)은 대손비용 증가, 순이자마진 감소 등으로 전년(6조 20억원) 대비 3351억원(5.6%↓) 감소했고, 경제사업부문도 국내 경기 부진 등으로 적자 규모가 지난 2022년(-2조 8744억원)과 비교해 2023년(-3조 6262억원)에 더욱 확대됐다. 이와함께 상호금융업계의 2023년말 기준 연체율은 2.97%로 전년말(1.52%) 대비 +1.45%p 상승했다. 가계대출 연체율은 1.53%로 전년말(0.91%) 대비 +0.62%p 상승했으며 기업대출 연체율은 4.31%로 전년말(2.23%) 대비 +2.08%p 상승했다. 고정이하여신비율은 3.41%로 전년말(1.84%) 대비 +1.57%p 상승했다. ⓒ금융감독원 금융감독원은 \"2023년말 연체율은 고금리 및 경기 회복 지연 등으로 차주의 채무상환능력이 약화되면서 전반적으로 상승했다\"며 \"이러한 연체율 상승은 코로나 위기이후 금리 인상, 자산 가격 조정 등 경제가 정상궤도로 회복하는 과정에서 수반되는 현상으로, 특히 저축은행의 경우 과거 위기시와 비교할 때 낮은 수준을 유지하고 있다\"고 분석했다. 지난 2011년 저축은행 사태 당시 연체율은 20.3%에 달했다는 것이다 . 또한 저축은행 및 상호금융 업권 모두 자본비율이 규제비율을 크게 상회하는 등 손실흡수능력도 양호한 수준이라고 안심시켰다. 영업실적이 상대적으로 부진한 저축은행 업권의 경우에도 모든 개별 저축은행이 규제비율 대비 3%p 수준을 상회하는 등 높은 자본비율을 유지 중이며 올해 저축은행 및 상호금융조합의 영업실적은 예금금리 안정화 등으로 전년보다 다소 개선될 가능성도 있다고 전망했다. 금감원은 다만 \"부동산 경기 회복 지연 등 대내·외 경제 불확실성에 대비해 대손충당금 추가 적립, 자본확충 등을 통해 손실흡수능력을 지속적으로 제고해 나가는 한편 경·공매, 캠코 및 자체 PF펀드 등을 통한 재구조화 등 다양한 방식의 매각, 채무 재조정 등을 통해 연체채권을 정리하는 등 건전성 관리를 지속할 예정\"이라고 밝혔다."

        println(extractKeyword(title, content))
    }

    private val k1 = 1.2
    private val b = 0.75


    fun extractKeyword(title: String, content: String): String {
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

        // BM25 점수 계산
        val wordBM25Scores = mutableMapOf<String, Double>()
        wordScores.forEach { (term, freq) ->
            val idf = calculateInverseDocumentFrequency(term) // IDF 계산 함수 필요
            val bm25Score = idf * ((freq * (k1 + 1)) / (freq + k1 * (1 - b + b * (docLength / avgDocLength))))
            wordBM25Scores[term] = bm25Score
        }

        return wordBM25Scores
    }

    // IDF 계산을 위한 임시 함수. 실제 환경에서는 문서 집합에 대한 IDF 값을 계산해야 합니다.
    private fun calculateInverseDocumentFrequency(term: String): Double {
        // 가정: 문서 집합에서의 용어 빈도수. 실제 환경에서는 이 값을 계산하거나 설정해야 합니다.
        val docFreq = 10
        val totalDocs = 10000
        return ln((totalDocs - docFreq + 0.5) / (docFreq + 0.5))
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
        private const val DEFAULT_FREQUENCY = 0.0
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
