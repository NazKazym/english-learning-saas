package com.clbboost.controller.auth

import com.clbboost.auth.model.JwtResponse
import com.clbboost.domain.user.User
import com.clbboost.repository.user.UserRepository
import com.clbboost.service.auth.JwtService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth/oauth2")
class OAuthController(
    private val userRepository: UserRepository,
    private val jwtService: JwtService
) {

    @GetMapping("/success")
    fun handleSuccess(@AuthenticationPrincipal user: OAuth2User): JwtResponse {
        val email = user.getAttribute<String>("email")
            ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email not found in OAuth2 response")

        // Check if user exists, otherwise create a new one
        val dbUser: User = userRepository.findByEmail(email.lowercase())
            ?: userRepository.save(User(email = email.lowercase()))

        val jwt = jwtService.generateToken(dbUser.id, dbUser.email)
        return JwtResponse(jwt)
    }

    @GetMapping("/failure")
    fun handleFailure(): ResponseEntity<String> =
        ResponseEntity.badRequest().body("OAuth login failed")
}
