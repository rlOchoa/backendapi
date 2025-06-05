package com.aria.backendapi.controller

import com.aria.backendapi.repository.UserRepository
import com.aria.backendapi.dto.UserResponseDTO
import com.aria.backendapi.dto.UserUpdateDTO
import com.aria.backendapi.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository,
    private val userService: UserService
) {
    @GetMapping("/me")
    fun getProfile(auth: Authentication): ResponseEntity<UserResponseDTO> {
        val email = auth.principal as String
        val user = userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Usuario no encontrado.") }

        return ResponseEntity.ok(
            UserResponseDTO(
                id = user.id,
                email = user.email,
                fullName = user.fullName,
                role = user.role.name
            )
        )
    }

    @PutMapping("/me")
    fun updateProfile(
        auth: Authentication,
        @RequestBody dto: UserUpdateDTO
    ): ResponseEntity<UserResponseDTO> {
        val email = auth.principal as String
        val updated = userService.updateProfile(email, dto)
        return ResponseEntity.ok(updated)
    }

    @DeleteMapping("/me")
    fun deleteProfile(auth: Authentication): ResponseEntity<Void> {
        val email = auth.principal as String
        userService.deleteUser(email)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllUsers(): List<UserResponseDTO> {
        return userService.getAllUsers()
    }
}