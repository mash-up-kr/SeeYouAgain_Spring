package com.mashup.shorts.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OkHttpClientConfig {

    @Bean
    fun okhttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}
