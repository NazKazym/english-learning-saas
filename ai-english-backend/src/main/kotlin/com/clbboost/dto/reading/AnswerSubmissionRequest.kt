package com.clbboost.dto.reading

import java.util.*

data class AnswerSubmissionRequest(
    val questionId: UUID,
    val optionId: UUID
)
