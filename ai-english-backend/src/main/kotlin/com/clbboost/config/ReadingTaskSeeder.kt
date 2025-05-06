package com.clbboost.config

import com.clbboost.domain.reading.ReadingOption
import com.clbboost.domain.reading.ReadingQuestion
import com.clbboost.domain.reading.ReadingTask
import com.clbboost.repository.reading.ReadingTaskRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class ReadingTaskSeeder(
    private val readingTaskRepository: ReadingTaskRepository
) {

    @PostConstruct
    @Transactional
    fun seedData() {
        if (readingTaskRepository.count() > 0) return

        // === Task 1 ===
        val task1 = ReadingTask(
            title = "Part 1 - Reading Correspondence",
            part = 1,
            level = "easy",
            passage = """
                Hi John,

                Just a reminder that the meeting has been moved to Tuesday at 10 a.m. Let me know if you can make it.

                Thanks,  
                Sarah
            """.trimIndent()
        )

        // Save task first so it has an ID
        val savedTask1 = readingTaskRepository.save(task1)

        val question1 = ReadingQuestion(
            task = savedTask1,
            questionText = "When is the meeting scheduled?",
            questionOrder = 1
        )

        val question2 = ReadingQuestion(
            task = savedTask1,
            questionText = "Who is the message addressed to?",
            questionOrder = 2
        )

        // Now that questions exist, add options referencing those questions
        question1.options = mutableListOf(
            ReadingOption(question = question1, optionLabel = 'A', optionText = "Monday at 10 a.m.", isCorrect = false),
            ReadingOption(question = question1, optionLabel = 'B', optionText = "Tuesday at 10 a.m.", isCorrect = true),
            ReadingOption(question = question1, optionLabel = 'C', optionText = "Wednesday at 9 a.m.", isCorrect = false),
            ReadingOption(question = question1, optionLabel = 'D', optionText = "Tuesday at 9 a.m.", isCorrect = false),
        )

        question2.options = mutableListOf(
            ReadingOption(question = question2, optionLabel = 'A', optionText = "Sarah", isCorrect = false),
            ReadingOption(question = question2, optionLabel = 'B', optionText = "John", isCorrect = true),
            ReadingOption(question = question2, optionLabel = 'C', optionText = "Michael", isCorrect = false),
            ReadingOption(question = question2, optionLabel = 'D', optionText = "Tom", isCorrect = false),
        )

        savedTask1.questions = mutableListOf(question1, question2)

        readingTaskRepository.save(savedTask1)

        // === Repeat for other tasks ===
        // You can apply the same pattern (save task → create questions → create options → assign → save task)

        // I can do the full version for all parts if you'd like.
    }
}
