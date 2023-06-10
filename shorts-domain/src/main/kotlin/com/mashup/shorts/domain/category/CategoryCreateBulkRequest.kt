package com.mashup.shorts.domain.category

/**
 * CategoryCreateBulkRequest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 10.
 */

data class CategoryCreateBulkRequest(
    val categoryNames: List<CategoryName>
)
