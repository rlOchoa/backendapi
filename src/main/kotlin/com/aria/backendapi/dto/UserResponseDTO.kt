package com.aria.backendapi.dto

data class UserResponseDTO(
    val id: Long,
    val email: String,
    val fullName: String,
    val role: String
)