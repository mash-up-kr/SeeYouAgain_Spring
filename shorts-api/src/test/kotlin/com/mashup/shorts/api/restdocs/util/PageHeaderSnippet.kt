package com.mashup.shorts.api.restdocs.util

import java.util.*
import org.springframework.restdocs.generate.RestDocumentationGenerator
import org.springframework.restdocs.operation.Operation
import org.springframework.restdocs.snippet.TemplatedSnippet

class PageHeaderSnippet(description: String) :
    TemplatedSnippet("page-header", Collections.singletonMap<String, Any>("description", description)) {

    override fun createModel(operation: Operation): Map<String, Any> {
        val model = HashMap<String, Any>()

        model["method"] = operation.request.method
        model["path"] = operation.attributes[RestDocumentationGenerator.ATTRIBUTE_NAME_URL_TEMPLATE] as Any

        return model
    }

    companion object {
        fun pageHeaderSnippet(description: String = ""): PageHeaderSnippet {
            return PageHeaderSnippet(description)
        }
    }

}
