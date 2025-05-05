package com.clbboost.controller.auth

import com.clbboost.auth.model.JwtResponse
import com.clbboost.auth.model.MagicLinkRequest
import com.clbboost.auth.model.VerifyMagicLinkRequest
import com.clbboost.service.auth.EmailService
import com.clbboost.service.auth.JwtService
import com.clbboost.service.auth.MagicLinkService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
@Validated
class MagicLinkController(
    private val emailService: EmailService,
    private val magicLinkService: MagicLinkService,
    private val jwtService: JwtService
) {

    @Value("\${app.magic-link.base-url}")
    lateinit var baseUrl: String

    @PostMapping("/magic-link", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun sendMagicLink(@Valid @RequestBody request: MagicLinkRequest) {
        val token = magicLinkService.generateMagicToken(request.email)

        val magicLinkUrl = "$baseUrl/verify?token=$token"

        emailService.sendMagicLinkEmail(request.email, magicLinkUrl)
    }

    @PostMapping("/verify-magic")
    fun verifyMagicLink(@Valid @RequestBody request: VerifyMagicLinkRequest): JwtResponse {
        val email = magicLinkService.verifyMagicToken(request.token)
            ?: throw IllegalArgumentException("Invalid or expired magic link token")

        val jwt = jwtService.generateToken(email)

        return JwtResponse(jwt)
    }


}
