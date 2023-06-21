package com.mashup.shorts.domain.memberCategory

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.config.aop.Auth
import com.mashup.shorts.config.aop.AuthContext
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.member.membercategory.MemberCategoryRetrieve

/**
 * MemberCategoryRetrieveApi
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 20.
 */
@RequestMapping("/v1/member/category")
@RestController
class MemberCategoryRetrieveApi(
    private val memberCategoryRetrieve: MemberCategoryRetrieve
) {

    @Auth
    @GetMapping
    fun retrieveMemberCategory(): ApiResponse<Map<String, List<CategoryName>>> {
        val member = AuthContext.getMember()
        return ApiResponse.success(HttpStatus.OK, mapOf("categories" to memberCategoryRetrieve.retrieveMemberCategory(member)))
    }
}
