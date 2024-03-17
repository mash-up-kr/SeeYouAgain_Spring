package com.mashup.shorts.core.leagcy.consts

import com.mashup.shorts.domain.category.CategoryName

internal const val POLITICS_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=100"
internal const val ECONOMY_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=101"
internal const val SOCIETY_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=102"
internal const val LIFE_CULTURE_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=103"
internal const val WORLD_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=104"
internal const val IT_SCIENCE_URL = "https://news.naver.com/main/main.naver?mode=LSD&mid=shm&sid1=105"
internal val categoryToUrl = mapOf(
    CategoryName.POLITICS to POLITICS_URL,
    CategoryName.ECONOMIC to ECONOMY_URL,
    CategoryName.SOCIETY to SOCIETY_URL,
    CategoryName.CULTURE to LIFE_CULTURE_URL,
    CategoryName.WORLD to WORLD_URL,
    CategoryName.SCIENCE to IT_SCIENCE_URL,
)

internal const val SYMBOLIC_LINK_BASE_URL = "https://news.naver.com"
