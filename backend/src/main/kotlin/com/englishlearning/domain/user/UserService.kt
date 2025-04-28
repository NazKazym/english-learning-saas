package com.englishlearning.domain.user

import com.englishlearning.domain.auth.AuthProvider
import com.englishlearning.infrastructure.persistence.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun findById(userId: UUID): User? =
        userRepository.findById(userId).orElse(null)

    @Transactional(readOnly = true)
    fun findByEmail(email: String): User? =
        userRepository.findByEmail(email.lowercase())

    /**
     * Called when authenticating via magic link or OAuth2.
     * If user exists, return it. Otherwise, create a new user.
     */
    @Transactional
    fun createOrGetUser(
        email: String,
        name: String? = null,
        pictureUrl: String? = null,
        provider: AuthProvider = AuthProvider.MAGIC_LINK,
        proficiencyLevel: ProficiencyLevel,
        testGoal: String
    ): User {
        val normalizedEmail = email.lowercase()
        val existingUser = userRepository.findByEmail(normalizedEmail)

        return existingUser ?: userRepository.save(
            User(
                email = normalizedEmail,
                name = name,
                pictureUrl = pictureUrl,
                authProvider = provider,
                isVerified = true, // assuming verified by email or OAuth
                proficiencyLevel = proficiencyLevel,
                testGoal = testGoal
            )
        )
    }

    @Transactional
    fun updateUserProfile(
        userId: UUID,
        proficiencyLevel: ProficiencyLevel?,
        testGoal: String?
    ): User {
        val user = userRepository.findById(userId)
            .orElseThrow { UserNotFoundException("User not found with ID: $userId") }

        val updatedUser = user.copy(
            proficiencyLevel = proficiencyLevel ?: user.proficiencyLevel,
            testGoal = testGoal ?: user.testGoal
        )

        return userRepository.save(updatedUser)
    }
}

class UserNotFoundException(message: String) : RuntimeException(message)
class UserAlreadyExistsException(message: String) : RuntimeException(message)
