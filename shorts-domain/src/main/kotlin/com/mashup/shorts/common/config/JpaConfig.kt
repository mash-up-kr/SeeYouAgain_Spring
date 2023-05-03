package com.mashup.shorts.common.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

/**
 * JpaConfig
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 04. 30.
 */
@EnableJpaAuditing
@Configuration
class JpaConfig(
    private val entityManager: EntityManager,
) {
    @Bean
    fun jpaQueryFactory() = JPAQueryFactory(entityManager)
}