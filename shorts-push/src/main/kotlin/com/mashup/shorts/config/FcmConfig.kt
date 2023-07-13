package com.usw.sugo.global.infrastructure.fcm.config

import java.io.IOException
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode

@Configuration
class FcmConfig {

    @Value("\${fcm.project-id}")
    private val projectId: String? = null

    @Value("\${fcm.secret}")
    private val secretKey: String? = null

    @Bean
    fun firebaseApp(): FirebaseApp {
        val resource = ClassPathResource(secretKey!!)
        try {
            resource.inputStream.use { stream ->
                val firebaseOptions: FirebaseOptions = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(stream))
                    .setProjectId(projectId)
                    .build()
                return FirebaseApp.initializeApp(firebaseOptions)
            }
        } catch (e: IOException) {
            throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E500_INTERNAL_SERVER_ERROR,
                resultErrorMessage = "FCM을 연동하는 중 서버 내부 오류가 발생했습니다."
            )
        }
    }
}
