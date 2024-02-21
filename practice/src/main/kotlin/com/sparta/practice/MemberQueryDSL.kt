package com.sparta.practice

import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.support.PageableExecutionUtils

class MemberQueryDSL : MemberRepositoryCustom, QueryDslSupport() {

    private val mem = QMember.member
    private val team = QTeam.team

    override fun search(condition: MemberSearchCondition): List<MemberTeamDto> {
        return queryFactory
            .select(QMemberTeamDto(
                mem.id,
                mem.userName,
                mem.age,
                team.id,
                team.teamName
            ))
            .from(mem)
            .leftJoin(mem.team, team)
            .where(
                userNameEq2(condition.userName),
                teamNameEq(condition.teamName),
                ageGoe(condition.ageGoe),
                ageLoe(condition.ageLoe)
            )
            .fetch()
    }

    override fun searchPageV1(condition: MemberSearchCondition, pageable: Pageable): Page<MemberTeamDto> {
        val fetchResults = queryFactory
            .select(
                QMemberTeamDto(
                    mem.id,
                    mem.userName,
                    mem.age,
                    team.id,
                    team.teamName
                )
            )
            .from(mem)
            .leftJoin(mem.team, team)
            .where(
                userNameEq2(condition.userName),
                teamNameEq(condition.teamName),
                ageGoe(condition.ageGoe),
                ageLoe(condition.ageLoe)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetchResults()

        val results = fetchResults.results
        val total = fetchResults.total

        return PageImpl(results,pageable,total)

    }

    override fun searchPageV2(condition: MemberSearchCondition, pageable: Pageable): Page<MemberTeamDto> {
        val fetch = queryFactory
            .select(
                QMemberTeamDto(
                    mem.id,
                    mem.userName,
                    mem.age,
                    team.id,
                    team.teamName
                )
            )
            .from(mem)
            .leftJoin(mem.team, team)
            .where(
                userNameEq2(condition.userName),
                teamNameEq(condition.teamName),
                ageGoe(condition.ageGoe),
                ageLoe(condition.ageLoe)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val fetchCount = queryFactory
            .select(
                QMemberTeamDto(
                    mem.id,
                    mem.userName,
                    mem.age,
                    team.id,
                    team.teamName
                )
            )
            .from(mem)
            .leftJoin(mem.team, team)
            .where(
                userNameEq2(condition.userName),
                teamNameEq(condition.teamName),
                ageGoe(condition.ageGoe),
                ageLoe(condition.ageLoe)
            )
            .fetchCount()

        return PageImpl(fetch,pageable,fetchCount)
    }

     fun searchPageV3(condition: MemberSearchCondition, pageable: Pageable): Page<MemberTeamDto> {
        val content = queryFactory
            .select(
                QMemberTeamDto(
                    mem.id,
                    mem.userName,
                    mem.age,
                    team.id,
                    team.teamName
                )
            )
            .from(mem)
            .leftJoin(mem.team, team)
            .where(
                userNameEq2(condition.userName),
                teamNameEq(condition.teamName),
                ageGoe(condition.ageGoe),
                ageLoe(condition.ageLoe)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count = queryFactory
            .select(
                QMemberTeamDto(
                    mem.id,
                    mem.userName,
                    mem.age,
                    team.id,
                    team.teamName
                )
            )
            .from(mem)
            .leftJoin(mem.team, team)
            .where(
                userNameEq2(condition.userName),
                teamNameEq(condition.teamName),
                ageGoe(condition.ageGoe),
                ageLoe(condition.ageLoe)
            )

        return PageableExecutionUtils.getPage(content,pageable,count::fetchCount)
    }

    private fun userNameEq2(userName: String?): BooleanExpression? {
        if(StringUtils.isNullOrEmpty(userName)){
            return null
        }
        return mem.userName.eq(userName)
    }

    private fun teamNameEq(teamName: String?): BooleanExpression? {
        if(StringUtils.isNullOrEmpty(teamName)){
            return null
        }
        return team.teamName.eq(teamName)
    }

    private fun ageLoe(ageLoe: Int?): BooleanExpression? {
        if(ageLoe == null){
            return null
        }
        return mem.age.loe(ageLoe)
    }

    private fun ageGoe(ageGoe: Int?): BooleanExpression? {
        if(ageGoe == null){
            return null
        }
        return mem.age.goe(ageGoe)
    }
}