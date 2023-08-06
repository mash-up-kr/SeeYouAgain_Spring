package com.mashup.shorts.domain.my.statistics

import java.time.LocalDate

data class ShortsCntByDate(
    val date: LocalDate,
    val shortsCnt: Int
)
