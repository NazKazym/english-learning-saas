package com.clbboost.domain.reading

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "reading_tasks")
data class ReadingTask(
    @Id @GeneratedValue val id: UUID? = null,
    val title: String,
    val part: Int, // 1 to 4 (CELPIP parts)
    val level: String, // e.g., easy, medium, hard
    @Column(columnDefinition = "TEXT") val passage: String,
    val diagramUrl: String? = null,

    @OneToMany(mappedBy = "task", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val questions: List<ReadingQuestion> = emptyList()
)
