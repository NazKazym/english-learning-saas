package com.clbboost.domain.reading


import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "reading_questions")
data class ReadingQuestion(
    @Id @GeneratedValue val id: UUID? = null,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    val task: ReadingTask,

    @Column(columnDefinition = "TEXT") val questionText: String,
    val questionOrder: Int,

    @OneToMany(mappedBy = "question", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val options: List<ReadingOption> = emptyList()
)
