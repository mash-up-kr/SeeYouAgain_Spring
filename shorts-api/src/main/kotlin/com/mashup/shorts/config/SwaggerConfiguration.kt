package com.mashup.shorts.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info


@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "숏스 API",
        description = "숏스 API DOCS",
        version = "v1"
    )
)
class SwaggerConfiguration {
    @Bean
    fun groupedOpenApi(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("숏스-api")
            .packagesToScan("com.mashup")
            .build()
    }
}
