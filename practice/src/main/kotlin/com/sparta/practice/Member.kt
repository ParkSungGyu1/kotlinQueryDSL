package com.sparta.practice

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Member (
    @Column
    var userName : String,
    @Column
    var age : Int
){
    @Id @GeneratedValue
    val id : Long = 0L
}