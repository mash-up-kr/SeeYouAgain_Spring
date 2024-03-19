package com.mashup.shorts.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager

@EnableJpaAuditing
@Configuration
class JpaConfig(
    private val entityManager: EntityManager,
) {
    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(entityManager)
}
