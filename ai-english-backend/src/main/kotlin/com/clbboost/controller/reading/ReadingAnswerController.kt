package com.clbboost.controller.reading

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
        @AuthenticationPrincipal user: User,
        @RequestBody request: AnswerSubmissionRequest
    ): AnswerSubmissionResponse {
        return userAnswerService.submitAnswer(user, request)
    }
}