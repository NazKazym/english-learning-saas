package com.englishprep.auth.controller

import com.englishprep.auth.service.MagicLinkService
import com.englishprep.auth.model.VerifyMagicLinkRequest
import com.englishprep.auth.model.JwtResponse
import com.englishprep.auth.model.MagicLinkRequest
import com.englishprep.auth.service.EmailService
import com.englishprep.auth.service.JwtService
import org.springframework.beans.factory.annotation.Value

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class MagicLinkController(
    private val emailService: EmailService,
    private val magicLinkService: MagicLinkService,
    private val jwtService: JwtService
) {

    @Value("\${app.magic-link.base-url}")
    lateinit var baseUrl: String

    @PostMapping("/magic-link")
    fun sendMagicLink(@RequestBody request: MagicLinkRequest) {
        val token = magicLinkService.generateMagicToken(request.email)

        val magicLinkUrl = "$baseUrl/verify?token=$token"

        emailService.sendMagicLinkEmail(request.email, magicLinkUrl)
    }

    @PostMapping("/verify-magic")
    fun verifyMagicLink(@RequestBody request: VerifyMagicLinkRequest): JwtResponse {
        val email = magicLinkService.verifyMagicToken(request.token)
            ?: throw IllegalArgumentException("Invalid or expired magic link token")

        val jwt = jwtService.generateToken(email)

        return JwtResponse(jwt)
    }


}
