package com.mashup.shorts.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * WebMvcConfig
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 05. 14.
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/shorts/**")
            .addResourceLocations("classpath:/static/")
    }
}
