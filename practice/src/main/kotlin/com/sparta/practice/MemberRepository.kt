package com.sparta.practice

import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long>, MemberRepositoryCustom {
    fun findByTeam(team: Team) : List<Member>
}