package com.mashup.shorts.core.modern.consts

import com.mashup.shorts.domain.category.CategoryName

private const val BASE_URL = "https://news.naver.com/section/"

private fun getUrl(section: String) = "$BASE_URL$section"

internal val categoryToUrl = mapOf(
    CategoryName.POLITICS to getUrl("100"),
    CategoryName.ECONOMIC to getUrl("101"),
    CategoryName.SOCIETY to getUrl("102"),
    CategoryName.CULTURE to getUrl("103"),
    CategoryName.WORLD to getUrl("104"),
    CategoryName.SCIENCE to getUrl("105"),
)

internal const val SYMBOLIC_LINK_BASE_URL = "https://news.naver.com"
