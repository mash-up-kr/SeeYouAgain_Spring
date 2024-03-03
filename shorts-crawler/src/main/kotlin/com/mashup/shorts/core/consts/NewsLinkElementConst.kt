package com.mashup.shorts.core.consts

import com.mashup.shorts.domain.category.CategoryName

object NewsLinkElementConst {

    private const val MORE_HEADLINE_LINKS_ELEMENT = "sa_text_cluster"

    val moreHeadLineLinksElements: Map<CategoryName, String> by lazy {
        CategoryName.values().associate { it to MORE_HEADLINE_LINKS_ELEMENT }
    }
}
