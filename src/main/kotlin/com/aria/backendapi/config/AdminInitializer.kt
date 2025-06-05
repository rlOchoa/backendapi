// Archivo: AdminInitializer.kt
package com.aria.backendapi.config

import com.aria.backendapi.model.Role
import com.aria.backendapi.model.User
import com.aria.backendapi.repository.UserRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class AdminInitializer {

    @Bean
    fun initAdminUser(
        userRepository: UserRepository,
        passwordEncoder: PasswordEncoder
    ) = CommandLineRunner {
        val adminEmail = "admin@aria.com"

        if (!userRepository.existsByEmail(adminEmail)) {
            val admin = User(
                fullName = "Admin Aria",
                email = adminEmail,
                password = passwordEncoder.encode("admin123"),
                role = Role.ADMIN
            )
            userRepository.save(admin)
            println("✅ Usuario administrador creado: $adminEmail / admin123")
        } else {
            println("ℹ️ Usuario administrador ya existe.")
        }
    }
}