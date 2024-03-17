package com.mashup.shorts.core.v2.keywordextractor

import org.springframework.stereotype.Component

@Component
interface KeywordExtractor {
    fun extractKeyword(title: String, content: String): String
}
