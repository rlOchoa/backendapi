package com.aria.backendapi.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import com.aria.backendapi.model.Role

data class UserRegisterDTO(
    @field:NotBlank(message = "El nombre completo es obligatorio.")
    val fullName: String,

    @field:Email(message = "El correo no es válido.")
    @field:NotBlank(message = "El correo es obligatorio.")
    val email: String,

    @field:Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres.")
    val password: String,
    val role : Role? = null
)
