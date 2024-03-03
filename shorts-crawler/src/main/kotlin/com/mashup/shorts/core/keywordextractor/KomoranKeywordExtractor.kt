package com.mashup.shorts.core.keywordextractor

import java.util.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran

@Component
@Qualifier("KomoranKeywordExtractor")
class KomoranKeywordExtractor : KeywordExtractor {

    override fun extractKeyword(content: String): String {
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
