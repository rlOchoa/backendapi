package com.aria.backendapi.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtUtil {

    private val jwtSecret = "SuperClaveJWTMuySeguraDeMinimo256BitsParaFirmar!!"
    private val expirationMs = 3600000 // 1 hora

    fun generateToken(email: String, role: String): String {
        val now = Date()
        val expiryDate = Date(now.time + expirationMs)

        return Jwts.builder()
            .setSubject(email)
            .claim("role", role)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(Keys.hmacShaKeyFor(jwtSecret.toByteArray()), SignatureAlgorithm.HS256)
            .compact()
    }

    fun extractEmail(token: String): String? {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(jwtSecret.toByteArray())
                .build()
                .parseClaimsJws(token)
                .body
                .subject
        } catch (e: Exception) {
            null
        }
    }
}