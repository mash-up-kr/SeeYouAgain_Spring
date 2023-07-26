package com.mashup.shorts.domain.membernewscard

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernewscard.QMemberNewsCard.memberNewsCard
import com.mashup.shorts.domain.membernewscard.dtomapper.MemberStatisticsByNewsCard
import com.mashup.shorts.domain.membernewscard.dtomapper.QMemberStatisticsByNewsCard
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class MemberNewsCardQueryDSLRepositoryImpl(
    private val queryFactory: JPAQueryFactory
):MemberNewsCardQueryDSLRepository {

    override fun retrieveStatisticsByMemberAndTargetDate(
        member: Member,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): MutableList<MemberStatisticsByNewsCard> {
        return queryFactory
            .select(
                QMemberStatisticsByNewsCard(
                    memberNewsCard.category.name.stringValue(),
                    memberNewsCard.count(),
                ))
            .from(memberNewsCard)
            .where(memberNewsCard.createdAt.between(
                startDateTime,
                endDateTime
            ))
            .where(memberNewsCard.member.eq(member))
            .groupBy(memberNewsCard.category)
            .fetch()
    }
}