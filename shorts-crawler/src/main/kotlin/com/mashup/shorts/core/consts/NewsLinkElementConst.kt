package com.mashup.shorts.core.consts

import com.mashup.shorts.domain.category.CategoryName


private const val POLITICS_MORE_HEADLINE_LINKS_ELEMENT = "sa_text_cluster"
private const val ECONOMIC_MORE_HEADLINE_LINKS_ELEMENT = "sa_text_cluster"
private const val SOCIETY_MORE_HEADLINE_LINKS_ELEMENT = "sa_text_cluster"
private const val LIFE_CULTURE_MORE_HEADLINE_LINKS_ELEMENT = "sa_text_cluster"
private const val WORLD_MORE_HEADLINE_LINKS_ELEMENT = "sa_text_cluster"
private const val IT_SCIENCE_MORE_HEADLINE_LINKS_ELEMENT = "sa_text_cluster"

internal val moreHeadLineLinksElements = mapOf(
    CategoryName.POLITICS to POLITICS_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.ECONOMIC to ECONOMIC_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.SOCIETY to SOCIETY_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.CULTURE to LIFE_CULTURE_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.WORLD to WORLD_MORE_HEADLINE_LINKS_ELEMENT,
    CategoryName.SCIENCE to IT_SCIENCE_MORE_HEADLINE_LINKS_ELEMENT
)
