package com.clbboost.dto.reading

import java.util.*

data class ReadingOptionDTO(
    val id: UUID,
    val optionLabel: Char,
    val optionText: String
)