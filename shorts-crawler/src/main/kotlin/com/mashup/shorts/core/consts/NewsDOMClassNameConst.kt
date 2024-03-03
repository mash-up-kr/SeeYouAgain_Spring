package com.mashup.shorts.core.consts

import com.mashup.shorts.domain.category.CategoryName

internal const val DETAIL_POLITICS_DOC_CLASS_NAME = "sa_text_title"
internal const val DETAIL_ECONOMIC_DOC_CLASS_NAME = "sa_text_title"
internal const val DETAIL_SOCIETY_DOC_CLASS_NAME = "sa_text_title"
internal const val DETAIL_LIFE_CULTURE_DOC_CLASS_NAME = "sa_text_title"
internal const val DETAIL_WORLD_DOC_CLASS_NAME = "sa_text_title"
internal const val DETAIL_IT_SCIENCE_DOC_CLASS_NAME = "sa_text_title"

internal val detailDocClassNames = mapOf(
    CategoryName.POLITICS to DETAIL_POLITICS_DOC_CLASS_NAME,
    CategoryName.ECONOMIC to DETAIL_ECONOMIC_DOC_CLASS_NAME,
    CategoryName.SOCIETY to DETAIL_SOCIETY_DOC_CLASS_NAME,
    CategoryName.CULTURE to DETAIL_LIFE_CULTURE_DOC_CLASS_NAME,
    CategoryName.WORLD to DETAIL_WORLD_DOC_CLASS_NAME,
    CategoryName.SCIENCE to DETAIL_IT_SCIENCE_DOC_CLASS_NAME
)

internal const val TITLE_CLASS_NAME = "media_end_head_headline"
internal const val CONTENT_CLASS_NAME = "go_trans _article_content"
internal const val IMAGE_ID_NAME = "img1"
internal const val PRESS_CLASS_NAME = "media_end_linked_more_point"
internal const val WRITTEN_DATETIME_CLASS_NAME = "media_end_head_info_datestamp_time _ARTICLE_DATE_TIME"

internal const val HEADLINE = "HEADLINE"
internal const val NORMAL = "NORMAL"
