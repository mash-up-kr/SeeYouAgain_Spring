package com.mashup.shorts.leagcy.keywordextractor

import java.util.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import com.mashup.shorts.core.keywordextractor.KeywordExtractor
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran

@Component
@Deprecated("Replaces morphological analysis with Komoran Library.")
@Qualifier("KomoranKeywordExtractor")
class KomoranKeywordExtractor : KeywordExtractor {

    override fun extractKeyword(title: String, content: String): String {
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
