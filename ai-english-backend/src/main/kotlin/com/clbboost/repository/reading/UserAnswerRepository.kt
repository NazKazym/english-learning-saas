package com.clbboost.repository.reading

import com.clbboost.domain.reading.UserAnswer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserAnswerRepository : JpaRepository<UserAnswer, UUID> {
    fun findAllByUserId(userId: UUID): List<UserAnswer>
}