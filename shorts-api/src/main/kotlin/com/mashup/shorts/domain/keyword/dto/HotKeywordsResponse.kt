package com.mashup.shorts.domain.keyword.dto

data class HotKeywordsResponse(
    val createdAt: String,
    val ranking: List<String>,
) {

    companion object {
        fun of(createdAt: String, ranking: List<String>): HotKeywordsResponse {
            return HotKeywordsResponse(createdAt = createdAt, ranking = ranking)
        }
    }
}
