package com.englishlearning.domain.auth

import com.englishlearning.domain.user.User
import com.englishlearning.domain.user.UserService
import com.englishlearning.infrastructure.external.EmailService
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.security.Key
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

@Service
class AuthService(
    private val userService: UserService,
    private val emailService: EmailService,
    private val redisTemplate: RedisTemplate<String, String>,
    @Value("\${app.security.jwt.secret}") private val jwtSecret: String,
    @Value("\${app.security.jwt.issuer}") private val jwtIssuer: String,
    @Value("\${app.security.jwt.expiration}") private val jwtExpirationMs: Long,
    @Value("\${app.email.magic-link-expiration}") private val magicLinkExpirationSeconds: Long
) {
    private val secretKey: Key by lazy {
        // Use a proper key size for HS256 (256 bits = 32 bytes)
        val decodedKey = jwtSecret.toByteArray()
        SecretKeySpec(decodedKey, 0, decodedKey.size, "HmacSHA256")
    }

    fun generateJwtToken(user: User): AuthToken {
        val now = Instant.now()
        val expiresAt = now.plusMillis(jwtExpirationMs)

        val token = Jwts.builder()
            .subject(user.id.toString())
            .issuer(jwtIssuer)
            .issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .claim("email", user.email)
            .claim("subscription", user.subscriptionStatus.name)
            .signWith(secretKey)
            .compact()

        return AuthToken(token, user.id, expiresAt)
    }

    fun validateToken(token: String): UserPrincipal? {
        try {
            // Check if token is blacklisted
            val isBlacklisted = redisTemplate.hasKey("jwt:blacklist:$token") ?: false
            if (isBlacklisted) {
                return null
            }

            val claims: Jws<Claims> = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)

            val userId = UUID.fromString(claims.body.subject)
            val email = claims.body["email"] as String
            val subscription = claims.body["subscription"] as String

            return UserPrincipal(
                userId = userId,
                email = email,
                subscriptionStatus = subscription,
                authorities = setOf("ROLE_USER")
            )
        } catch (e: JwtException) {
            return null
        } catch (e: IllegalArgumentException) {
            return null
        }
    }

    fun invalidateToken(token: String) {
        try {
            val claims: Jws<Claims> = Jwts.builder()
                .signWith(secretKey)
                .
                .parseClaimsJws(token)

            val expirationDate = claims.payload.expiration
            val now = Date()
            val timeToLive = expirationDate.time - now.time

            if (timeToLive > 0) {
                redisTemplate.opsForValue().set(
                    "jwt:blacklist:$token",
                    "blacklisted",
                    Duration.ofMillis(timeToLive)
                )
            }
        } catch (e: Exception) {
            // Token is already invalid, no need to blacklist
        }
    }

    fun generateMagicLink(email: String): Boolean {
        val user = userService.findByEmail(email) ?: return false

        val token = generateRandomToken()
        val expiresAt = Instant.now().plusSeconds(magicLinkExpirationSeconds)

        // Store token in Redis with expiration
        redisTemplate.opsForValue().set(
            "magic-link:$token",
            user.email,
            Duration.ofSeconds(magicLinkExpirationSeconds)
        )

        return emailService.sendMagicLink(user.email, token)
    }

    fun verifyMagicLink(token: String): User? {
        val email = redisTemplate.opsForValue().get("magic-link:$token") ?: return null

        // Delete token after use (one-time use)
        redisTemplate.delete("magic-link:$token")

        return userService.findByEmail(email)
    }

    private fun generateRandomToken(): String {
        val bytes = ByteArray(32)
        Random.nextBytes(bytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes)
    }
}