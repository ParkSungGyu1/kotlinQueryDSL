package com.sparta.practice

import com.querydsl.core.annotations.QueryProjection

data class MemberTeamDto @QueryProjection constructor(
    val memberId : Long,
    val userName : String,
    val age : Int,
    val teamId : Long,
    val teamName : String
)