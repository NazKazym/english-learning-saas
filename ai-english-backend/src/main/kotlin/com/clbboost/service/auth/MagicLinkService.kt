package com.clbboost.service.auth

import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class MagicLinkService {

    private val tokenStore = ConcurrentHashMap<String, MagicToken>()

    private val tokenExpiryMinutes = 15L

    fun generateMagicToken(email: String): String {
        val token = UUID.randomUUID().toString()
        val expiry = Instant.now().plusSeconds(tokenExpiryMinutes * 60)

        tokenStore[token] = MagicToken(email, expiry)
        return token
    }

    fun verifyMagicToken(token: String): String? {
        val magicToken = tokenStore[token]

        if (magicToken == null || magicToken.expiry.isBefore(Instant.now())) {
            return null
        }

        // Remove token after successful verification
        tokenStore.remove(token)

        return magicToken.email
    }

    private data class MagicToken(
        val email: String,
        val expiry: Instant
    )
}
