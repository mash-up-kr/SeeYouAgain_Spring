package com.mashup.shorts.domain.category

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * CategoryRetrieve
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 11.
 */
@Transactional(readOnly = true)
@Service
class CategoryRetrieve(
    private val categoryRepository: CategoryRepository
) {

    fun getCategories(categoryNames: List<CategoryName>): List<Category> {
        return categoryRepository.findByNameIn(categoryNames)
    }
}
