package com.mashup.shorts.core.infrastructure

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.service.annotation.HttpExchange
import org.springframework.web.service.annotation.PostExchange

@HttpExchange(url = "https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize")
interface ClovaRequestInterface {

    @PostExchange
    fun summaryNews(
        @RequestBody clovaSummaryRequestBodyForm: ClovaSummaryRequestBodyForm,
        @RequestHeader("X-NCP-APIGW-API-KEY-ID") clientId: String,
        @RequestHeader("X-NCP-APIGW-API-KEY") clientSecret: String,
        @RequestHeader("Content-Type") contentType: String,
    ): Map<Any, Any>
}
