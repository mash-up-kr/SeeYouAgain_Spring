package com.mashup.shorts.domain.membernewscard.dtomapper

import com.mashup.shorts.domain.newscard.NewsCard
import com.querydsl.core.annotations.QueryProjection

data class MemberTodayShorts(
    var numberOfShorts: Int,
    var numberOfReadShorts: Int,
    var memberShorts: List<NewsCard>,
)

data class MemberStatisticsByNewsCard @QueryProjection constructor(
    val categoryName: String,
    val count: Long
)