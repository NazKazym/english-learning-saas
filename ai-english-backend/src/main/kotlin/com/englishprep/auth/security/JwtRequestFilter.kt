package com.englishprep.auth.security

import com.englishprep.auth.service.AuthService
import com.englishprep.common.logger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

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
                val userEmail = authService.extractEmail(jwt)

                log.info("Checking JWT token for email: $userEmail")
                if (userEmail != null && SecurityContextHolder.getContext().authentication == null) {
                    val authToken = UsernamePasswordAuthenticationToken(
                        userEmail,
                        null,
                        emptyList() // ✅ No roles/authorities for now (simple auth)
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
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
