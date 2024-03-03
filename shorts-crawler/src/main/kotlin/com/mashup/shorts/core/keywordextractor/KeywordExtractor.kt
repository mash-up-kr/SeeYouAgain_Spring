package com.mashup.shorts.core.keywordextractor

import org.springframework.stereotype.Component

@Component
interface KeywordExtractor {
    fun extractKeyword(content: String): String
}
