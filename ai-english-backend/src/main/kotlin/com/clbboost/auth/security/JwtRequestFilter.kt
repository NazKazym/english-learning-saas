package com.clbboost.auth.security

import com.clbboost.service.auth.AuthService
import com.clbboost.auth.model.UserPrincipal
import com.clbboost.util.logger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*

@Component
class JwtRequestFilter(
    private val authService: AuthService
) : OncePerRequestFilter() {

    private val log = logger()

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            val jwt = authHeader.substring(7)

            try {
                val userId = authService.extractUserId(jwt)
                val email = authService.extractEmail(jwt)

                log.info("JWT for user ID: $userId, email: $email")

                if (SecurityContextHolder.getContext().authentication == null) {
                    val user = authService.loadUserById(userId)

                    val principal = UserPrincipal(
                        id = user.id!!,
                        email = user.email,
                        roles = listOf("USER") // Optional: load from DB if needed
                    )

                    val authentication: AbstractAuthenticationToken = object : AbstractAuthenticationToken(principal.authorities) {
                        override fun getCredentials(): Any? = null
                        override fun getPrincipal(): Any = principal
                    }.apply {
                        details = WebAuthenticationDetailsSource().buildDetails(request)
                        isAuthenticated = true
                    }

                    SecurityContextHolder.getContext().authentication = authentication
                }

            } catch (ex: ExpiredJwtException) {
                log.warn("JWT token expired: ${ex.message}")
            } catch (ex: MalformedJwtException) {
                log.warn("Invalid JWT token: ${ex.message}")
            } catch (ex: SignatureException) {
                log.warn("JWT signature invalid: ${ex.message}")
            } catch (ex: Exception) {
                log.warn("JWT processing failed: ${ex.message}")
            }
        }

        filterChain.doFilter(request, response)
    }
}
