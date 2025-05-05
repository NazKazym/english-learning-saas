package com.clbboost.domain.reading


import com.clbboost.domain.user.User
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "user_answers")
data class UserAnswer(
    @Id @GeneratedValue val id: UUID? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    val question: ReadingQuestion,

    val selectedOption: Char,
    val isCorrect: Boolean,
    val submittedAt: Date = Date()
)
