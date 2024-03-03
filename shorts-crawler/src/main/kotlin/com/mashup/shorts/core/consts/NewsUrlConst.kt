package com.mashup.shorts.core.consts

import com.mashup.shorts.domain.category.CategoryName

private const val POLITICS_URL = "https://news.naver.com/section/100"
private const val ECONOMY_URL = "https://news.naver.com/section/101"
private const val SOCIETY_URL = "https://news.naver.com/section/102"
private const val LIFE_CULTURE_URL = "https://news.naver.com/section/103"
private const val WORLD_URL = "https://news.naver.com/section/104"
private const val IT_SCIENCE_URL = "https://news.naver.com/section/105"

internal val categoryToUrl = mapOf(
    CategoryName.POLITICS to POLITICS_URL,
    CategoryName.ECONOMIC to ECONOMY_URL,
    CategoryName.SOCIETY to SOCIETY_URL,
    CategoryName.CULTURE to LIFE_CULTURE_URL,
    CategoryName.WORLD to WORLD_URL,
    CategoryName.SCIENCE to IT_SCIENCE_URL,
)

internal const val SYMBOLIC_LINK_BASE_URL = "https://news.naver.com"
