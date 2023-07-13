package com.mashup.shorts.domain.member

import kotlin.random.Random
import com.mashup.shorts.common.util.RandomWordsNickname
import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table


@Table(name = "member")
@Entity
class Member(
    @Column(name = "unique_id", nullable = false, length = 100)
    val uniqueId: String,

    @Column(name = "nickname", nullable = false, length = 20)
    val nickname: String,

    @Column(name = "fcm_token_payload", nullable = false, length = 200)
    var fcmTokenPayload: String,

    @Column(name = "is_allowed_alarm", nullable = false)
    var isAllowedAlarm: Boolean = true,
) : BaseEntity() {
    companion object {
        fun generateNickname(): String {
            val randomAdjective = RandomWordsNickname.adjectives[Random.nextInt(RandomWordsNickname.adjectives.size)]
            val randomNoun = RandomWordsNickname.nouns[Random.nextInt(RandomWordsNickname.nouns.size)]
            return "$randomAdjective $randomNoun"
        }
    }
}
