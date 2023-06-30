package com.mashup.shorts.domain.home.memberCategory

import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.mashup.shorts.common.aop.Auth
import com.mashup.shorts.common.aop.AuthContext
import com.mashup.shorts.common.response.ApiResponse
import com.mashup.shorts.domain.category.CategoryName
import com.mashup.shorts.domain.membercategory.MemberCategoryRetrieve

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
    private val memberCategoryRetrieve: MemberCategoryRetrieve,
) {

    @Auth
    @GetMapping
    fun retrieveMemberCategory(): ApiResponse<Map<String, List<CategoryName>>> {
        val member = AuthContext.getMember()
        return ApiResponse.success(
            OK,
            mapOf("categories" to memberCategoryRetrieve.retrieveMemberCategory(member))
        )
    }
}
