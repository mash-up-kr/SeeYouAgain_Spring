package com.mashup.shorts.core.util

import com.mashup.shorts.core.const.NewsDOMClassNameConst.HEADLINE
import com.mashup.shorts.core.const.NewsDOMClassNameConst.NORMAL

object CrawlerContentConverter {

    internal fun convertHeadLine(headLineFlag: Boolean): String {
        return if (headLineFlag) {
            HEADLINE
        } else {
            NORMAL
        }
    }
}
