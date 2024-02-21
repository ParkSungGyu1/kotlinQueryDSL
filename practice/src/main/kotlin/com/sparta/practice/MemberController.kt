package com.sparta.practice

import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import jakarta.transaction.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MemberController(
    private val memberRepository: MemberRepository
) :  QueryDslSupport()  {

    private val mem = QMember.member
    private val team = QTeam.team

    //BooleanBuilder 사용
    @GetMapping("/practice1")
    fun dynamicQueryBooleanBuilder() : List<Member>{
        val userName = "유저네임1"
        val age = 10
        return searchMember1(userName,age)
    }

    fun searchMember1(userName : String?, age : Int?) : List<Member>{
        val builder = BooleanBuilder()

        if(userName != null){
            builder.and(mem.userName.eq(userName))
        }

        if(age != null){
            builder.and(mem.age.eq(age))
        }

        return queryFactory
                    .selectFrom(mem)
                    .where(builder)
                    .fetch()
    }

    //다중 WHere 사용
    @GetMapping("/practice2")
    fun dynamicQueryWhere() : List<Member>{
        val userName = "유저네임1"
        val age = 10
        return searchMember2(userName,age)
    }

    @Transactional
    fun searchMember2(userName: String?, age: Int?): List<Member> {

        val list = queryFactory
            .selectFrom(mem)
            .where(allEq(userName,age))
            .fetch()

        return queryFactory
            .selectFrom(mem)
            .where(allEq(userName,age))
            .fetch()
    }

    // userName 이 null인지 체크하고 아니라면 where 쌓기
    fun userNameEq(userName: String?) : BooleanExpression?{
        if(userName == null){
            return null
        }
        return mem.userName.eq(userName)
    }

    fun ageEq(age: Int?) : BooleanExpression?{
        if(age == null){
            return null
        }
        return mem.age.eq(age)
    }

    fun allEq(userName: String?, age: Int?) : BooleanExpression?{
        return userNameEq(userName)?.and(ageEq(age))
    }

    // 컴파일 시점 런타임 시점

    // 캐싱

    /*
    1. queryDSL 성능 굉장히 좋음 : spring data jpa ==> JPQL (@Query("select * from member")) ===> QueryDSL 사용감이 좋다 개발이 편리하다
    2. queryDSL 1차 캐시 x : 1차캐시를 활용해야하는 코드나 조회가 좀 민감한 (돈) 직접 쿼리를 작성하는 JPQL이나 entityManager createQuery
    3. 간단한 조회 findById findAll spring data jpa
    4. 동적 쿼리 queryDSL
    )*/


    /**
     * 동적쿼리와 성능 최적화 : BooleanBuilder
     */
    @GetMapping("/practice3")
    fun practice3() : List<MemberTeamDto>{
        val condition = MemberSearchCondition("", "", 5,10)
        val builder = BooleanBuilder()

        if (!StringUtils.isNullOrEmpty(condition.userName)){
            builder.and(mem.userName.eq(condition.userName))
        }
        if (!StringUtils.isNullOrEmpty(condition.teamName)){
            builder.and(team.teamName.eq(condition.teamName))
        }
        if (condition.ageGoe != null){
            builder.and(mem.age.goe(condition.ageGoe))
        }
        if (condition.ageLoe != null){
            builder.and(mem.age.loe(condition.ageLoe))
        }

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
            .where(builder)
            .fetch()
    }

    /**
     * 동적쿼리와 성능 최적화 : BooleanExpression
     */
    @GetMapping("/practice4")
    fun practice4() : List<MemberTeamDto>{
        val condition = MemberSearchCondition("", "", 5,15)


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

    @GetMapping("/practice5")
    fun practice5() : List<MemberTeamDto>{

        val condition = MemberSearchCondition("", "", 5,15)

        return memberRepository.search(condition)
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