package com.mashup.shorts.core.util

import org.springframework.stereotype.Component

@Component
class CrawlerContentFilter {

    internal fun filterSquareBracket(target: String): String {
        return target
            .replace("[", "")
            .replace("]", "")
    }

    internal fun filterImageLinkForm(rawImageLink: String): String {
        return rawImageLink
            .substringAfter("data-src=\"")
            .substringBefore("\"")
    }
}
