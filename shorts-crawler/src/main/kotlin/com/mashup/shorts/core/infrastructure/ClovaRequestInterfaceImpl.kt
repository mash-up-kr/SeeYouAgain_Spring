package com.mashup.shorts.core.infrastructure

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Component
class ClovaRequestInterfaceImpl : ClovaRequestInterface {

    private final val client: WebClient = WebClient.builder()
        .baseUrl("https://naveropenapi.apigw.ntruss.com/text-summary/v1/summarize")
        .build()
    private final val factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client))
        .build()
    private final val clovaRequestInterface = factory.createClient(ClovaRequestInterface::class.java)

    override fun summaryNews(
        clovaSummaryRequestBodyForm: ClovaSummaryRequestBodyForm,
        clientId: String,
        clientSecret: String,
        contentType: String,
    ): Map<Any, Any> {

        return clovaRequestInterface.summaryNews(
            clovaSummaryRequestBodyForm,
            clientId,
            clientSecret,
            contentType
        )

    }
}
