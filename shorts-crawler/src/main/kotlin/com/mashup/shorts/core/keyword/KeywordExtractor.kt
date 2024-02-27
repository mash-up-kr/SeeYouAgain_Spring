package com.mashup.shorts.core.keyword

import org.springframework.stereotype.Component

@Component
interface KeywordExtractor {
    fun extractKeyword(content: String): String
}
