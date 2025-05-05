package com.clbboost.service.auth

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService(
    @Value("\${app.jwt.secret}")
    private val jwtSecret: String
) {

    fun generateToken(email: String): String {
        val now = Date()
        val expiry = Date(now.time + 1000 * 60 * 60 * 24) // 24 hours

        val secretKey = Keys.hmacShaKeyFor(jwtSecret.toByteArray())

        return Jwts.builder()
            .subject(email)
            .issuedAt(now)
            .expiration(expiry)
            .signWith(secretKey)
            .compact()
    }
}
