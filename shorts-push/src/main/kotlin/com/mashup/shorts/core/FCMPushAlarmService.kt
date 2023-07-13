package com.mashup.shorts.core

import org.springframework.stereotype.Service
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.mashup.shorts.common.exception.ShortsBaseException
import com.mashup.shorts.common.exception.ShortsErrorCode
import com.mashup.shorts.domain.member.Member
import com.usw.sugo.global.infrastructure.fcm.config.FcmConfig


@Service
class FCMPushAlarmService {

    private val firebaseApp = FcmConfig().firebaseApp()
    fun send(form: FCMPushAlarmForm) {
        val notification = Notification.builder()
            .setTitle(form.title)
            .setImage(form.imageURL)
            .setBody(form.content)
            .build()

        try {
            form.members.map {
                val message =
                    Message.builder()
                        .setNotification(notification)
                        .setToken(getMemberAgreedAlarm(it))
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

    private fun getMemberAgreedAlarm(member: Member): String? {
        return if (member.isAllowedAlarm) return member.fcmTokenPayload
        else {
            return null
        }
    }
}

