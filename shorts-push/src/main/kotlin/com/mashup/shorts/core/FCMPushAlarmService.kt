package com.mashup.shorts.core

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.mashup.shorts.exception.ShortsBaseException
import com.mashup.shorts.exception.ShortsErrorCode
import com.mashup.shorts.config.FcmConfig
import org.springframework.stereotype.Service


@Service
class FCMPushAlarmService {

    private val firebaseApp = FcmConfig().firebaseApp()
    fun send(form: FCMPushAlarmForm) {
        val targetMembers = form.members.filter { it.isAllowedAlarm }
        val notification = Notification.builder()
            .setTitle(form.title)
            .setImage(form.imageURL)
            .setBody(form.content)
            .build()
        try {
            targetMembers.map {
                val message = Message.builder()
                        .setNotification(notification)
                        .setToken(it.fcmTokenPayload)
                        .build()
                FirebaseMessaging
                    .getInstance(firebaseApp)
                    .sendAsync(message)
            }
        } catch (e: FirebaseMessagingException) {
            throw ShortsBaseException.from(
                shortsErrorCode = ShortsErrorCode.E500_INTERNAL_SERVER_ERROR,
                resultErrorMessage = "푸쉬 알림을 전송하는 중 ${e.message} 문제가 발생했습니다."
            )
        }
    }
}

