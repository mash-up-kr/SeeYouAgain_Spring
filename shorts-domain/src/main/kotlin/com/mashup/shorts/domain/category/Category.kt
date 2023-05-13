package com.mashup.shorts.domain.category

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * Category
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
@Table(name = "category")
@Entity
class Category(
    @Column(name = "name")
    val name: String
): BaseEntity()
