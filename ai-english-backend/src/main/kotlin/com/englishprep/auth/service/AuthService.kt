package com.englishprep.auth.service

import com.englishprep.common.logger
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import javax.crypto.SecretKey

@Service
class AuthService(
    @Value("\${app.jwt.secret}")
    private val jwtSecret: String
) {
    private val log = logger()

    // Lazily build a SecretKey from your secret
    private val secretKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(jwtSecret.toByteArray())
    }

    /**
     * Parses the JWT and returns the subject (usually the user's email)
     * or null if the token is invalid/expired.
     */
    fun extractEmail(token: String): String? {
        return try {
            Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .payload
                .subject
        } catch (exc: Exception) {
            log.error(exc.message)
            throw exc
        }
    }
}
