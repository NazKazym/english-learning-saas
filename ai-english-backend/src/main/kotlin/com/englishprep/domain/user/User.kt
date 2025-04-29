package com.englishprep.domain.user

import com.englishprep.domain.auth.AuthProvider
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "users")
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val proficiencyLevel: ProficiencyLevel,

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