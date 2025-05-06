package com.clbboost.auth.model

import com.clbboost.domain.user.ProficiencyLevel
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*


data class VerifyMagicLinkRequest(
    @field:NotBlank(message = "Token must not be blank")
    val token: String
)

data class MagicLinkRequest(
    @field:NotBlank(message = "Email must not be blank")
    @field:Email(message = "Invalid email format")
    val email: String
)

data class JwtResponse(
    @field:NotBlank(message = "JWT must not be blank")
    val jwt: String
)

class UserPrincipal(
    val id: UUID,
    private val email: String,
    val roles: List<String> = listOf("USER")
) : UserDetails {

    override fun getUsername(): String = email
    override fun getPassword(): String = "" // not used

    override fun getAuthorities(): Collection<GrantedAuthority> =
        roles.map { SimpleGrantedAuthority("ROLE_$it") }

    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}


data class UpdateUserRequest(
    val name: String?,
    val pictureUrl: String?,
    val proficiencyLevel: ProficiencyLevel?
)
