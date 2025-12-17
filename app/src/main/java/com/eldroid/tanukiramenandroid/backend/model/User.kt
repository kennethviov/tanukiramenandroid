package com.eldroid.tanukiramenandroid.backend.model

data class User(
    val userId: Long,
    val username: String,
    val name: String,
    val role: Role,
    val roleName: String
)
