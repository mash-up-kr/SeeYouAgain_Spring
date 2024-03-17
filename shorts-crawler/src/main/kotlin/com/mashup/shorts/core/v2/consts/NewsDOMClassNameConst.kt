package com.mashup.shorts.core.v2.consts

import com.mashup.shorts.domain.category.CategoryName

object NewsDOMClassNameConst {
    private const val DETAIL_DOC_CLASS_NAME = "sa_text_title"
    const val TITLE_CLASS_NAME = "media_end_head_headline"
    const val CONTENT_CLASS_NAME = "go_trans _article_content"
    const val IMAGE_ID_NAME = "img1"
    const val PRESS_CLASS_NAME = "media_end_linked_more_point"
    const val WRITTEN_DATETIME_CLASS_NAME = "media_end_head_info_datestamp_time _ARTICLE_DATE_TIME"
    const val HEADLINE = "HEADLINE"
    const val NORMAL = "NORMAL"

    val detailDocClassNames: Map<CategoryName, String> by lazy {
        CategoryName.values().associateWith { DETAIL_DOC_CLASS_NAME }
    }
}
