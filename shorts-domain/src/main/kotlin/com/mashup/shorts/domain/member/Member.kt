package com.mashup.shorts.domain.member

import com.mashup.shorts.util.RandomWordsNickname
import com.mashup.shorts.domain.BaseEntity
import jakarta.persistence.*
import kotlin.random.Random

@Table(name = "member")
@Entity
class Member(
    @Column(name = "unique_id", nullable = false, length = 100)
    val uniqueId: String,

    @Column(name = "nickname", nullable = false, length = 20)
    var nickname: String,

    @Column(name = "fcm_token_payload", nullable = false, length = 200)
    var fcmTokenPayload: String,

    @Column(name = "is_allowed_alarm", nullable = false)
    var isAllowedAlarm: Boolean = true,

    @Enumerated(value = EnumType.STRING)
    var showMode: ShowMode = ShowMode.NORMAL,
) : BaseEntity() {
    companion object {
        fun generateNickname(): String {
            val randomAdjective = RandomWordsNickname.adjectives[Random.nextInt(RandomWordsNickname.adjectives.size)]
            val randomNoun = RandomWordsNickname.nouns[Random.nextInt(RandomWordsNickname.nouns.size)]
            return "$randomAdjective $randomNoun"
        }
    }

    fun changeShowMode(showMode: ShowMode) {
        this.showMode = showMode
    }

    fun modifyNickname(nickname: String) {
        this.nickname = nickname
    }
}
