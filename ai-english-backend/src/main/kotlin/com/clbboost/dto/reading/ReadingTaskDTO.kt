package com.clbboost.dto.reading

import java.util.*

data class ReadingTaskDTO(
    val id: UUID,
    val title: String,
    val part: Int,
    val level: String,
    val passage: String,
    val diagramUrl: String?,
    val questions: List<ReadingQuestionDTO>
)
