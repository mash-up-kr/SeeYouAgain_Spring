package com.mashup.shorts.legacycore.keyword

import org.springframework.stereotype.Component

@Component
interface KeywordExtractor {
    fun extractKeyword(content: String): String
}
