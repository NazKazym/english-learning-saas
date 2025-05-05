package com.clbboost.service.reading

import com.clbboost.domain.reading.UserAnswer
import com.clbboost.dto.reading.AnswerSubmissionRequest
import com.clbboost.dto.reading.AnswerSubmissionResponse
import com.clbboost.repository.reading.ReadingOptionRepository
import com.clbboost.repository.reading.ReadingQuestionRepository
import com.clbboost.repository.reading.UserAnswerRepository
import com.clbboost.repository.user.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserAnswerService(
    private val userRepository: UserRepository,
    private val userAnswerRepository: UserAnswerRepository,
    private val questionRepository: ReadingQuestionRepository,
    private val optionRepository: ReadingOptionRepository
) {

    fun submitAnswer(userId : UUID, request: AnswerSubmissionRequest): AnswerSubmissionResponse {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalStateException("User not found") }
        val question = questionRepository.findById(request.questionId)
            .orElseThrow { IllegalArgumentException("Question not found") }

        val selectedOption = optionRepository.findById(request.optionId)
            .orElseThrow { IllegalArgumentException("Option not found") }

        val isCorrect = selectedOption.isCorrect

        val answer = UserAnswer(
            user = user,
            question = question,
            selectedOption = selectedOption.optionLabel,
            isCorrect = isCorrect
        )

        userAnswerRepository.save(answer)

        return AnswerSubmissionResponse(
            correct = isCorrect,
            explanation = if (isCorrect) "Correct!" else "Incorrect. Try reviewing the passage again."
        )
    }
}
