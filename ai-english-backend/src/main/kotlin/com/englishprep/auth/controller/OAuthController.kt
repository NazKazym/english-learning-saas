package com.englishprep.auth.controller

import com.englishprep.auth.model.JwtResponse
import com.englishprep.auth.service.JwtService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.core.user.OAuth2User
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth/oauth2")
class OAuthController(
    private val jwtService: JwtService
) {
    @GetMapping("/success")
    fun handleSuccess(@AuthenticationPrincipal user: OAuth2User): JwtResponse {
        val email = user.getAttribute<String>("email") ?: throw RuntimeException("Email not found")
        val jwt = jwtService.generateToken(email)
        return JwtResponse(jwt)
    }

    @GetMapping("/failure")
    fun handleFailure(): ResponseEntity<String> = ResponseEntity.badRequest().body("OAuth login failed")
}
