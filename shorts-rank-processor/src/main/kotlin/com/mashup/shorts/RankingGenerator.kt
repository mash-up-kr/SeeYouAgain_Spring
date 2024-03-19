package com.mashup.shorts

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import com.mashup.shorts.domain.keyword.HotKeyword
import com.mashup.shorts.domain.keyword.HotKeywordRepository

@Component
@Transactional
class RankingGenerator(
    private val hotKeywordRepository: HotKeywordRepository,
) {

    fun saveKeywordRanking(keywordsCountingPair: Map<String, Int>) {
        val sortedKeywords = keywordsCountingPair.toList().sortedByDescending { it.second }
        val keywordRanking = StringBuilder()

        val rankingSize = if (sortedKeywords.size < 10) sortedKeywords.size else 10
        for (rank: Int in 0 until rankingSize) {
            keywordRanking.append(sortedKeywords[rank]).append(", ")
        }

        hotKeywordRepository.save(HotKeyword(keywordRanking = keywordRanking.toString()))
    }
}
