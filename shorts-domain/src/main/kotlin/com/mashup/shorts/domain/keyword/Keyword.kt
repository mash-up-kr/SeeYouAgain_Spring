package com.mashup.shorts.domain.keyword

import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

/**
 * Keyword
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 13.
 */
@Table(name = "keyword")
@Entity
class Keyword(
    @Column(name = "name")
    val name: String
): BaseEntity()