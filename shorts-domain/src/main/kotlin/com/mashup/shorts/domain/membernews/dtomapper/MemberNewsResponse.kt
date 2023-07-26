package com.mashup.shorts.domain.membernews.dtomapper

import com.querydsl.core.annotations.QueryProjection

data class MemberStatisticsByNews @QueryProjection constructor(
    val categoryName: String,
    val count: Long
)