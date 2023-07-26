package com.mashup.shorts.domain.membernews

import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.membernews.QMemberNews.memberNews
import com.mashup.shorts.domain.membernews.dtomapper.MemberStatisticsByNews
import com.mashup.shorts.domain.membernews.dtomapper.QMemberStatisticsByNews
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
class MemberNewsQueryDSLRepositoryImpl(
    private val queryFactory: JPAQueryFactory
):MemberNewsQueryDSLRepository {

    override fun retrieveStatisticsByMemberAndTargetDate(
        member: Member,
        startDateTime: LocalDateTime,
        endDateTime: LocalDateTime
    ): MutableList<MemberStatisticsByNews> {
        return queryFactory
            .select(
                QMemberStatisticsByNews(
                memberNews.category.name.stringValue(),
                memberNews.count()
            ))
            .from(memberNews)
            .where(memberNews.createdAt.between(
                startDateTime,
                endDateTime
            ))
            .where(memberNews.member.eq(member))
            .groupBy(memberNews.category)
            .fetch()
    }
}