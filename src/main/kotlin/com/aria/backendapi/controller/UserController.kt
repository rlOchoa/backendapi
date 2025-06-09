package com.aria.backendapi.controller

import com.aria.backendapi.repository.UserRepository
import com.aria.backendapi.dto.UserResponseDTO
import com.aria.backendapi.dto.UserUpdateDTO
import com.aria.backendapi.service.ProfileImageService
import com.aria.backendapi.service.UserService
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.server.ResponseStatusException
import java.security.Principal

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userRepository: UserRepository,
    private val userService: UserService,
    private val profileImageService: ProfileImageService
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

    @PutMapping("/{id}/profile-picture")
    fun uploadProfilePicture(
        @PathVariable id: Long,
        @RequestParam("file") file: MultipartFile,
        principal: Principal
    ): ResponseEntity<Any> {
        val user = userRepository.findById(id).orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND) }

        // Validación de acceso: propio usuario o admin
        if (user.email != principal.name && !userService.isAdmin(principal.name)) {
            throw ResponseStatusException(HttpStatus.FORBIDDEN, "No autorizado para modificar esta imagen.")
        }

        val fileName = profileImageService.saveProfileImage(file)
        user.profilePictureUrl = "/api/users/profile-picture/$fileName"
        userRepository.save(user)

        return ResponseEntity.ok(mapOf("url" to user.profilePictureUrl))
    }

    @GetMapping("/profile-picture/{filename:.+}")
    fun getProfilePicture(@PathVariable filename: String): ResponseEntity<Resource> {
        val resource = profileImageService.loadImage(filename)
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"$filename\"")
            .body(resource)
    }
}