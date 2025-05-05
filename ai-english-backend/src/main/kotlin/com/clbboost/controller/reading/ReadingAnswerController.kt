package com.clbboost.controller.reading

import com.clbboost.auth.model.UserPrincipal
import com.clbboost.dto.reading.AnswerSubmissionRequest
import com.clbboost.dto.reading.AnswerSubmissionResponse
import com.clbboost.service.reading.UserAnswerService
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/reading-answers")
class ReadingAnswerController(
    private val userAnswerService: UserAnswerService
) {

    @PostMapping
    fun submitAnswer(
        @AuthenticationPrincipal user: UserPrincipal,
        @RequestBody request: AnswerSubmissionRequest
    ): AnswerSubmissionResponse {
        return userAnswerService.submitAnswer(user.id, request)
    }
}