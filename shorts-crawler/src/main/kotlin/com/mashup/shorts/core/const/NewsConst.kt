package com.mashup.shorts.core.const

import com.mashup.shorts.domain.category.CategoryName.CULTURE
import com.mashup.shorts.domain.category.CategoryName.ECONOMIC
import com.mashup.shorts.domain.category.CategoryName.POLITICS
import com.mashup.shorts.domain.category.CategoryName.SCIENCE
import com.mashup.shorts.domain.category.CategoryName.SOCIETY
import com.mashup.shorts.domain.category.CategoryName.WORLD

object NewsUrlConst {

    private const val POLITICS_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100"
    private const val ECONOMY_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101"
    private const val SOCIETY_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102"
    private const val LIFE_CULTURE_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=103"
    private const val WORLD_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=104"
    private const val IT_SCIENCE_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=105"
    val categoryToUrl = mapOf(
        POLITICS to POLITICS_URL,
        ECONOMIC to ECONOMY_URL,
        SOCIETY to SOCIETY_URL,
        CULTURE to LIFE_CULTURE_URL,
        WORLD to WORLD_URL,
        SCIENCE to IT_SCIENCE_URL,
    )

    const val SYMBOLIC_LINK_BASE_URL = "https://news.naver.com"
}

object NewsLinkElementConst {


    private const val POLITICS_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_pol.clstitle)"
    private const val ECONOMIC_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_eco.clstitle)"
    private const val SOCIETY_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_nav.clstitle)"
    private const val LIFE_CULTURE_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_lif.clstitle)"
    private const val WORLD_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_wor.clstitle)"
    private const val IT_SCIENCE_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_sci.clstitle)"

    val moreHeadLineLinksElements = mapOf(
        POLITICS to POLITICS_MORE_HEADLINE_LINKS_ELEMENT,
        ECONOMIC to ECONOMIC_MORE_HEADLINE_LINKS_ELEMENT,
        SOCIETY to SOCIETY_MORE_HEADLINE_LINKS_ELEMENT,
        CULTURE to LIFE_CULTURE_MORE_HEADLINE_LINKS_ELEMENT,
        WORLD to WORLD_MORE_HEADLINE_LINKS_ELEMENT,
        SCIENCE to IT_SCIENCE_MORE_HEADLINE_LINKS_ELEMENT
    )
}

object NewsDOMClassNameConst {

    private const val DETAIL_POLITICS_DOC_CLASS_NAME = "nclicks(cls_pol.clsart1)"
    private const val DETAIL_ECONOMIC_DOC_CLASS_NAME = "nclicks(cls_eco.clsart1)"
    private const val DETAIL_SOCIETY_DOC_CLASS_NAME = "nclicks(cls_nav.clsart1)"
    private const val DETAIL_LIFE_CULTURE_DOC_CLASS_NAME = "nclicks(cls_lif.clsart1)"
    private const val DETAIL_WORLD_DOC_CLASS_NAME = "nclicks(cls_wor.clsart1)"
    private const val DETAIL_IT_SCIENCE_DOC_CLASS_NAME = "nclicks(cls_sci.clsart1)"

    val detailDocClassNames = mapOf(
        POLITICS to DETAIL_POLITICS_DOC_CLASS_NAME,
        ECONOMIC to DETAIL_ECONOMIC_DOC_CLASS_NAME,
        SOCIETY to DETAIL_SOCIETY_DOC_CLASS_NAME,
        CULTURE to DETAIL_LIFE_CULTURE_DOC_CLASS_NAME,
        WORLD to DETAIL_WORLD_DOC_CLASS_NAME,
        SCIENCE to DETAIL_IT_SCIENCE_DOC_CLASS_NAME
    )

    const val TITLE_CLASS_NAME = "media_end_head_headline"
    const val CONTENT_CLASS_NAME = "go_trans _article_content"
    const val IMAGE_ID_NAME = "img1"
    const val PRESS_CLASS_NAME = "media_end_linked_more_point"
    const val WRITTEN_DATETIME_CLASS_NAME = "media_end_head_info_datestamp_time _ARTICLE_DATE_TIME"

    const val HEADLINE = "HEADLINE"
    const val NORMAL = "NORMAL"
}
