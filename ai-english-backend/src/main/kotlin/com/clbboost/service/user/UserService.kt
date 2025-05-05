package com.clbboost.service.user

import com.clbboost.auth.model.UpdateUserRequest
import com.clbboost.domain.auth.AuthProvider
import com.clbboost.domain.user.ProficiencyLevel
import com.clbboost.domain.user.User
import com.clbboost.repository.user.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Transactional(readOnly = true)
    fun getById(id: UUID): User =
        userRepository.findById(id).orElseThrow {
            EntityNotFoundException("User with ID $id not found")
        }


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
        proficiencyLevel: ProficiencyLevel
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
                proficiencyLevel = proficiencyLevel
            )
        )
    }

    @Transactional
    fun update(userId: UUID, request: UpdateUserRequest): User {
        val user = userRepository.findById(userId)
            .orElseThrow { NoSuchElementException("User not found") }

        val updatedUser = user.copy(
            name = request.name ?: user.name,
            pictureUrl = request.pictureUrl ?: user.pictureUrl,
            proficiencyLevel = request.proficiencyLevel ?: user.proficiencyLevel
        )

        return userRepository.save(updatedUser)
    }
}

class UserNotFoundException(message: String) : RuntimeException(message)
class UserAlreadyExistsException(message: String) : RuntimeException(message)
