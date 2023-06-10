package com.mashup.shorts.core.util

import org.springframework.stereotype.Component
import com.mashup.shorts.core.const.NewsDOMClassNameConst

@Component
class CrawlerContentConverter {

    internal fun convertHeadLine(headLineFlag: Boolean): String {
        return if (headLineFlag) {
            NewsDOMClassNameConst.HEADLINE
        } else {
            NewsDOMClassNameConst.NORMAL
        }
    }
}
