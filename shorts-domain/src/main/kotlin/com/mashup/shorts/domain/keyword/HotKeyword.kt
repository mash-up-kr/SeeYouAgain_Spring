package com.mashup.shorts.domain.keyword

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "hot_keyword")
@Entity
class HotKeyword(

    @Column(name = "keyword_ranking", nullable = false, length = 200)
    var keywordRanking: String

) : BaseEntity()
