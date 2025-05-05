package com.clbboost.service.auth

import com.clbboost.domain.user.User
import com.clbboost.repository.user.UserRepository
import com.clbboost.util.logger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class AuthService(
    @Value("\${app.jwt.secret}")
    private val jwtSecret: String,
    private val userRepository: UserRepository
) {
    private val log = logger()

    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    /**
     * Parses JWT and extracts user ID (UUID) from the `sub` claim.
     */
    fun extractUserId(token: String): UUID {
        return try {
            val claims = parseToken(token)
            UUID.fromString(claims.subject)
        } catch (e: Exception) {
            log.error("Failed to extract user ID: ${e.message}")
            throw e
        }
    }

    /**
     * Parses JWT and extracts user email from the `email` claim.
     */
    fun extractEmail(token: String): String {
        return try {
            val claims = parseToken(token)
            claims["email"] as? String ?: throw IllegalStateException("Email claim missing")
        } catch (e: Exception) {
            log.error("Failed to extract email: ${e.message}")
            throw e
        }
    }

    /**
     * Loads a user from DB using UUID. Throws if not found.
     */
    fun loadUserById(userId: UUID): User {
        return userRepository.findById(userId)
            .orElseThrow { IllegalStateException("User not found with ID: $userId") }
    }

    private fun parseToken(token: String) =
        Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
