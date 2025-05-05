package com.clbboost.domain.reading

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "reading_options")
data class ReadingOption(
    @Id @GeneratedValue val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    val question: ReadingQuestion,

    val optionLabel: Char, // 'A', 'B', 'C', 'D'
    @Column(columnDefinition = "TEXT") val optionText: String,
    val isCorrect: Boolean
)
