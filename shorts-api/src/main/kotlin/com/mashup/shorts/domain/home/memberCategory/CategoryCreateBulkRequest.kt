package com.mashup.shorts.domain.home.memberCategory

import com.mashup.shorts.domain.category.CategoryName

/**
 * CategoryCreateBulkRequest
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 11.
 */
data class CategoryCreateBulkRequest(
    val categoryNames: List<CategoryName>
)
