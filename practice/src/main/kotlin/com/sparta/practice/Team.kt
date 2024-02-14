package com.sparta.practice

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Team (
    @Column(name = "team_name")
    val teamName : String
) {
    @Id @GeneratedValue
    val id : Long = 0L
}