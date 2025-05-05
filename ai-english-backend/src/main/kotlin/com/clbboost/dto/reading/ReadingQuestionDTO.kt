package com.clbboost.dto.reading

import java.util.*

data class ReadingQuestionDTO(
    val id: UUID,
    val questionText: String,
    val questionOrder: Int,
    val options: List<ReadingOptionDTO>
)
