package com.sparta.practice

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MemberRepositoryCustom {
    fun search(condition: MemberSearchCondition) : List<MemberTeamDto>
    fun searchPageV1(condition: MemberSearchCondition, pageable: Pageable) : Page<MemberTeamDto>
    fun searchPageV2(condition: MemberSearchCondition, pageable: Pageable) : Page<MemberTeamDto>
}