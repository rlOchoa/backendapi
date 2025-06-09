package com.aria.backendapi.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String, // ya encriptada

    @Column(nullable = false)
    var fullName: String,

    @Enumerated(EnumType.STRING)
    val role: Role = Role.USER,

    @Column(name = "profile_picture_url")
    var profilePictureUrl: String? = null

)

enum class Role {
    USER,
    ADMIN,
    GUEST
}