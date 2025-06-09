package com.aria.backendapi.service

import com.aria.backendapi.dto.*
import com.aria.backendapi.model.Role
import com.aria.backendapi.model.User
import com.aria.backendapi.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.aria.backendapi.util.JwtUtil
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @Transactional
    fun register(dto: UserRegisterDTO): UserResponseDTO {
        if (userRepository.existsByEmail(dto.email)) {
            throw IllegalArgumentException("El correo ya está registrado.")
        }

        val hashedPassword = passwordEncoder.encode(dto.password)

        val newUser = User(
            email = dto.email,
            password = hashedPassword,
            fullName = dto.fullName,
            role = dto.role ?: Role.USER // ahora puede ser ADMIN si se especifica
        )

        val saved = userRepository.save(newUser)

        return UserResponseDTO(
            id = saved.id,
            email = saved.email,
            fullName = saved.fullName,
            role = saved.role.name
        )
    }

    fun login(dto: LoginRequestDTO): LoginResponseDTO {
        val user = userRepository.findByEmail(dto.email)
            .orElseThrow { InvalidCredentialsException("Credenciales incorrectas.") }

        if (!passwordEncoder.matches(dto.password, user.password)) {
            throw InvalidCredentialsException("Credenciales incorrectas.")
        }

        val token = jwtUtil.generateToken(user.email, user.role.name)
        return LoginResponseDTO(token)
    }

    fun updateProfile(email: String, dto: UserUpdateDTO): UserResponseDTO {
        val user = userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Usuario no encontrado.") }

        user.fullName = dto.fullName
        val saved = userRepository.save(user)

        return UserResponseDTO(
            id = saved.id,
            email = saved.email,
            fullName = saved.fullName,
            role = saved.role.name
        )
    }

    fun deleteUser(email: String) {
        val user = userRepository.findByEmail(email)
            .orElseThrow { RuntimeException("Usuario no encontrado.") }

        userRepository.delete(user)
    }

    fun getAllUsers(): List<UserResponseDTO> {
        return userRepository.findAll().map {
            UserResponseDTO(
                id = it.id,
                email = it.email,
                fullName = it.fullName,
                role = it.role.name
            )
        }
    }

    fun isAdmin(email: String): Boolean {
        val user = userRepository.findByEmail(email)
            .orElseThrow { ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado.") }
        return user.role.name == "ADMIN"
    }
}

class InvalidCredentialsException(message: String) : RuntimeException(message)