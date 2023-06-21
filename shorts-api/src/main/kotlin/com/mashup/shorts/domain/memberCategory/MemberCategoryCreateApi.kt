package com.mashup.shorts.domain.memberCategory

import java.util.*
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.member.membercategory.MemberCategoryCreate

/**
 * MemberCategoryCreateApi
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 11.
 */
@RequestMapping("/v1/member/category")
@RestController
class MemberCategoryCreateApi(
    private val memberCategoryCreate: MemberCategoryCreate
) {

    @PostMapping
    fun createCategory(@RequestBody categoryCreateBulkRequest: CategoryCreateBulkRequest): ApiResponse<Map<String, String>> {
        val memberUniqueId = UUID.randomUUID().toString()
        memberCategoryCreate.createCategory(categoryCreateBulkRequest.categoryNames, memberUniqueId)
        return ApiResponse.success(HttpStatus.CREATED, mapOf("uniqueId" to memberUniqueId))
    }

    @Auth
    @PutMapping
    fun modifyMemberCategory(@RequestBody categoryCreateBulkRequest: CategoryCreateBulkRequest): ApiResponse<Void> {
        val member = AuthContext.getMember()
        memberCategoryCreate.modifyMemberCategory(categoryCreateBulkRequest.categoryNames, member)
        return ApiResponse.success(HttpStatus.OK)
    }
}
