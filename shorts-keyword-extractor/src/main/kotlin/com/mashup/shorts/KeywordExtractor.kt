package com.mashup.shorts

import org.springframework.stereotype.Component

@Component
interface KeywordExtractor {
    fun extractKeyword(title: String, content: String): String
}
