package com.mashup.shorts.domain.hot.keyword.dto

import com.mashup.shorts.domain.keyword.KeywordRanking

class KeywordRankingMapper {

    companion object {

        fun keywordRankingToResponse(keywordRanking: KeywordRanking): HotKeywordsResponse {
            return HotKeywordsResponse(
                keywordRanking.createdAt,
                keywordRanking.ranking
            )
        }
    }
}
