package com.clbboost.repository.user

import com.clbboost.domain.auth.AuthToken
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime
import java.util.*

interface AuthTokenRepository : JpaRepository<AuthToken, UUID> {
    fun findByToken(token: String): AuthToken?
    fun deleteByUserId(userId: UUID)

    @Transactional
    @Modifying
    @Query("DELETE FROM AuthToken a WHERE a.expiresAt < :now")
    fun deleteExpiredTokens(now: LocalDateTime = LocalDateTime.now())
}
