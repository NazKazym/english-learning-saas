package com.englishlearning.domain.user

import com.englishlearning.domain.auth.AuthProvider
import jakarta.persistence.Column
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import java.time.LocalDateTime
import java.util.*

data class User(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    val email: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val authProvider: AuthProvider = AuthProvider.MAGIC_LINK,

    val name: String? = null,

    val pictureUrl: String? = null,

    var lastLoginAt: LocalDateTime? = null,

    var subscriptionStatus: SubscriptionStatus = SubscriptionStatus.FREE,

    @Column(name = "is_verified")
    var isVerified: Boolean = false,
)

enum class ProficiencyLevel {
    BEGINNER, INTERMEDIATE, ADVANCED
}

enum class SubscriptionStatus {
    FREE, PREMIUM, ENTERPRISE
}