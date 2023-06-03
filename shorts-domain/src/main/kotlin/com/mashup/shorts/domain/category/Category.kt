package com.mashup.shorts.domain.category

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Table

@Table(name = "category")
@Entity
class Category(
    @Column(name = "name", nullable = false, length = 15, unique = true)
    @Enumerated(EnumType.STRING)
    val name: CategoryName,
) : BaseEntity()
