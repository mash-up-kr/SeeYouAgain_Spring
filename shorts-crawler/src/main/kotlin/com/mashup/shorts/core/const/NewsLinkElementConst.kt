package com.mashup.shorts.core.const

import com.mashup.shorts.domain.category.CategoryName


internal const val POLITICS_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_pol.clstitle)"
internal const val ECONOMIC_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_eco.clstitle)"
internal const val SOCIETY_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_nav.clstitle)"
internal const val LIFE_CULTURE_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_lif.clstitle)"
internal const val WORLD_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_wor.clstitle)"
internal const val IT_SCIENCE_MORE_HEADLINE_LINKS_ELEMENT = "sh_head_more nclicks(cls_sci.clstitle)"

internal val moreHeadLineLinksElements = mapOf(
    CategoryName.POLITICS to POLITICS_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.ECONOMIC to ECONOMIC_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.SOCIETY to SOCIETY_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.CULTURE to LIFE_CULTURE_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.WORLD to WORLD_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.SCIENCE to IT_SCIENCE_MORE_HEADLINE_LINKS_ELEMENT
)
