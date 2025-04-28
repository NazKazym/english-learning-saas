package com.englishprep.auth.model

data class VerifyMagicLinkRequest(
    val token: String
)

data class MagicLinkRequest(
    val email: String
)

data class JwtResponse(
    val jwt: String
)