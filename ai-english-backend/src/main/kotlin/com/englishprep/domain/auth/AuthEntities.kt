package com.englishprep.domain.auth

import com.englishprep.domain.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.user.OAuth2User
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "auth_tokens")
data class AuthToken(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    val token: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,

    @Column(name = "expires_at", nullable = false)
    val expiresAt: LocalDateTime
)

@Entity
@Table(name = "magic_link_tokens")
data class MagicLinkToken(
    @Id
    val id: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    val token: String,

    @Column(nullable = false)
    val email: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    val expiresAt: LocalDateTime,

    var used: Boolean = false
)


class OAuth2UserPrincipal(
    private val user: User,
    private val attributes: Map<String, Any>
) : OAuth2User {

    override fun getName(): String = user.email

    override fun getAttributes(): Map<String, Any> = attributes

    override fun getAuthorities(): Collection<GrantedAuthority> = emptyList()

    fun getUser(): User = user
}


enum class AuthProvider {
    MAGIC_LINK, GOOGLE, FACEBOOK, LINKEDIN, GITHUB
}