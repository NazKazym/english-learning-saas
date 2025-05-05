package com.clbboost.infrastructure

import com.clbboost.repository.user.AuthTokenRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class AuthTokenCleaner(
    private val authTokenRepository: AuthTokenRepository
) {
    @Scheduled(cron = "0 0 * * * *") // every hour
    fun cleanExpiredTokens() {
        authTokenRepository.deleteExpiredTokens()
    }
}
