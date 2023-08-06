package com.mashup.shorts.domain.membernews

import java.time.LocalDateTime
import com.mashup.shorts.domain.member.Member
import com.mashup.shorts.domain.news.News
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import com.mashup.shorts.domain.my.statistics.ShortsCntByCategoryVo
import com.mashup.shorts.domain.my.statistics.ShortsCntByDateVo

@Repository
interface MemberNewsRepository : JpaRepository<MemberNews, Long> {

    fun deleteAllByMemberAndNewsIn(member: Member, newsList: List<News>)

    fun existsByMemberAndNews(member: Member, news: News): Boolean
    fun findByNewsIn(news: List<News>): List<MemberNews>

    fun countAllByMember(member: Member): Int

    fun findAllByMember(member: Member): List<MemberNews>

    fun findByMemberAndNews(member: Member, news: News): MemberNews?

    @Query(value = "select DATE_FORMAT(read_at, '%Y-%m-%d') date, count(*) shortsCnt " +
        "from member_news " +
        "where member_id = :memberId and " +
        "      read_at >= :startDateTime " +
        "group by date\n" +
        "order by date",
        nativeQuery = true)
    fun getShortsCntByDate(@Param("memberId") memberId: Long,
                           @Param("startDateTime") startDateTime: LocalDateTime): List<ShortsCntByDateVo>

    @Query(value = "select c.name category, count(*) shortsCnt\n" +
        "from member_news as mn\n" +
        "inner join news n on mn.news_id = n.id\n" +
        "inner join category c on n.category_id = c.id\n" +
        "where mn.member_id = :memberId and\n" +
        "      mn.read_at >= :startDateTime\n" +
        "group by n.category_id\n" +
        "order by shortsCnt desc",
        nativeQuery = true)
    fun getShortsCntByCategory(@Param("memberId") memberId: Long,
                               @Param("startDateTime") startDateTime: LocalDateTime): List<ShortsCntByCategoryVo>
}
