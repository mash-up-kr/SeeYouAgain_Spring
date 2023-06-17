package com.mashup.shorts.api.restdocs.util

import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor
import org.springframework.restdocs.operation.preprocess.Preprocessors
import org.springframework.restdocs.snippet.Attributes

/**
 * RestDocsUtils
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 06. 15.
 */
object RestDocsUtils {

    fun getDocumentRequest(): OperationRequestPreprocessor {
        return Preprocessors.preprocessRequest(
            Preprocessors.modifyUris()
                .scheme("http")
                .host("3.38.65.72")
                .port(8080),
            Preprocessors.prettyPrint()
        )
    }

    fun getDocumentResponse(): OperationResponsePreprocessor {
        return Preprocessors.preprocessResponse(Preprocessors.prettyPrint())
    }
}
