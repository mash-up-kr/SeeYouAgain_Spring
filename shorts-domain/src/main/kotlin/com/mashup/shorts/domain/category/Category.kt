package com.mashup.shorts.domain.category

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Table(name = "category")
@Entity
class Category(
    @Column(name = "name", nullable = false, length = 10, unique = true)
    val name: String,
) : BaseEntity()
