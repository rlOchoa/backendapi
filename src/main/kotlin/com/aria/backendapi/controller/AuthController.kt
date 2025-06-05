package com.aria.backendapi.controller

import com.aria.backendapi.dto.LoginRequestDTO
import com.aria.backendapi.dto.LoginResponseDTO
import com.aria.backendapi.dto.UserRegisterDTO
import com.aria.backendapi.dto.UserResponseDTO
import com.aria.backendapi.model.Role
import com.aria.backendapi.service.InvalidCredentialsException
import com.aria.backendapi.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody dto: UserRegisterDTO): ResponseEntity<UserResponseDTO> {
        val createdUser = userService.register(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser)
    }

    @PostMapping("/login")
    fun login(@RequestBody dto: LoginRequestDTO): ResponseEntity<LoginResponseDTO> {
        return try {
            val token = userService.login(dto)
            ResponseEntity.ok(token)
        } catch (e: InvalidCredentialsException) {
            ResponseEntity.status(401).build()
        }
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleDuplicate(e: IllegalArgumentException): ResponseEntity<String> {
        return ResponseEntity.badRequest().body(e.message)
    }

    @PostMapping("/register-admin")
    @PreAuthorize("hasRole('ADMIN')")
    fun registerAdmin(@RequestBody dto: UserRegisterDTO): UserResponseDTO {
        return userService.register(dto.copy(role = Role.ADMIN))
    }
}