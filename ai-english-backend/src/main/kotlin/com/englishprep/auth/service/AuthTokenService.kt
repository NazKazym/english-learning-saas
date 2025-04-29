package com.englishprep.auth.service

import com.englishprep.domain.auth.AuthToken
import com.englishprep.domain.user.User
import com.englishprep.infrastructure.persistence.AuthTokenRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID


@Service
class AuthTokenService(
    private val authTokenRepository: AuthTokenRepository
) {

    fun createAuthToken(user: User): AuthToken {

        // Optional: cleanup old tokens before creating new one
        cleanupExpiredTokens()

        val token = AuthToken(
            id = UUID.randomUUID(),
            token = UUID.randomUUID().toString(),
            user = user,
            expiresAt = LocalDateTime.now().plusMinutes(15) // 15 minutes validity
        )
        return authTokenRepository.save(token)
    }

    fun validateToken(token: String): AuthToken? {
        val authToken = authTokenRepository.findByToken(token)
        if (authToken != null && authToken.expiresAt.isAfter(LocalDateTime.now())) {
            return authToken
        }
        return null
    }

    fun invalidateToken(tokenId: UUID) {
        authTokenRepository.deleteById(tokenId)
    }

    fun cleanupExpiredTokens() {
        authTokenRepository.deleteExpiredTokens()
    }
}