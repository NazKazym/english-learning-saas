package com.clbboost.config

import com.clbboost.domain.reading.ReadingOption
import com.clbboost.domain.reading.ReadingQuestion
import com.clbboost.domain.reading.ReadingTask
import com.clbboost.repository.reading.ReadingTaskRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component

@Component
class ReadingTaskSeeder(
    private val readingTaskRepository: ReadingTaskRepository
) {

    @PostConstruct
    fun seedData() {
        if (readingTaskRepository.count() > 0) return

        // Step 1: Create task
        val task = ReadingTask(
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

        // Step 2: Create questions
        val question1 = ReadingQuestion(
            task = task,
            questionText = "When is the meeting scheduled?",
            questionOrder = 1
        )
        val question2 = ReadingQuestion(
            task = task,
            questionText = "Who is the message addressed to?",
            questionOrder = 2
        )

        // Step 3: Create options and link to questions
        val options1 = listOf(
            ReadingOption(optionLabel = 'A', optionText = "Monday at 10 a.m.", isCorrect = false, question = question1),
            ReadingOption(optionLabel = 'B', optionText = "Tuesday at 10 a.m.", isCorrect = true, question = question1),
            ReadingOption(optionLabel = 'C', optionText = "Wednesday at 9 a.m.", isCorrect = false, question = question1),
            ReadingOption(optionLabel = 'D', optionText = "Tuesday at 9 a.m.", isCorrect = false, question = question1),
        )

        val options2 = listOf(
            ReadingOption(optionLabel = 'A', optionText = "Sarah", isCorrect = false, question = question2),
            ReadingOption(optionLabel = 'B', optionText = "John", isCorrect = true, question = question2),
            ReadingOption(optionLabel = 'C', optionText = "Michael", isCorrect = false, question = question2),
            ReadingOption(optionLabel = 'D', optionText = "Tom", isCorrect = false, question = question2),
        )

        // Step 4: Assign options to questions
        val fullQuestion1 = question1.copy(options = options1)
        val fullQuestion2 = question2.copy(options = options2)

        // Step 5: Assign questions to task
        val fullTask = task.copy(questions = listOf(fullQuestion1, fullQuestion2))

        // Step 6: Save
        readingTaskRepository.save(fullTask)



        val task2 = ReadingTask(
            title = "Part 2 - Reading to Apply a Diagram",
            part = 2,
            level = "easy",
            passage = "Refer to the floor plan. Which room is directly connected to the kitchen?",
            diagramUrl = "https://example.com/diagrams/floorplan1.png"
        )

        val q2 = ReadingQuestion(
            task = task2,
            questionText = "Which room connects directly to the kitchen?",
            questionOrder = 1
        )

        val q2Options = listOf(
            ReadingOption(optionLabel = 'A', optionText = "Garage", isCorrect = false, question = q2),
            ReadingOption(optionLabel = 'B', optionText = "Dining Room", isCorrect = true, question = q2),
            ReadingOption(optionLabel = 'C', optionText = "Bathroom", isCorrect = false, question = q2),
            ReadingOption(optionLabel = 'D', optionText = "Bedroom", isCorrect = false, question = q2),
        )
        val fullQ2 = q2.copy(options = q2Options)



        val task3 = ReadingTask(
            title = "Part 3 - Reading for Information",
            part = 3,
            level = "medium",
            passage = """
        Visitors to the National Park are reminded that all hiking trails close at 6:00 p.m. 
        Please carry out all garbage, and respect the wildlife.
    """.trimIndent()
        )

        val q3 = ReadingQuestion(
            task = task3,
            questionText = "What time do the hiking trails close?",
            questionOrder = 1
        )

        val q3Options = listOf(
            ReadingOption(optionLabel = 'A', optionText = "4:00 p.m.", isCorrect = false, question = q3),
            ReadingOption(optionLabel = 'B', optionText = "5:00 p.m.", isCorrect = false, question = q3),
            ReadingOption(optionLabel = 'C', optionText = "6:00 p.m.", isCorrect = true, question = q3),
            ReadingOption(optionLabel = 'D', optionText = "7:00 p.m.", isCorrect = false, question = q3),
        )
        val fullQ3 = q3.copy(options = q3Options)


        val task4 = ReadingTask(
            title = "Part 4 - Reading for Viewpoints",
            part = 4,
            level = "medium",
            passage = """
        Alice: I think we should ban cars from downtown to make it pedestrian-friendly.
        Ben: That would hurt businesses relying on car access.
    """.trimIndent()
        )

        val q4 = ReadingQuestion(
            task = task4,
            questionText = "What is Ben's main concern?",
            questionOrder = 1
        )

        val q4Options = listOf(
            ReadingOption(optionLabel = 'A', optionText = "Air pollution", isCorrect = false, question = q4),
            ReadingOption(optionLabel = 'B', optionText = "Business impact", isCorrect = true, question = q4),
            ReadingOption(optionLabel = 'C', optionText = "Noise from pedestrians", isCorrect = false, question = q4),
            ReadingOption(optionLabel = 'D', optionText = "Parking costs", isCorrect = false, question = q4),
        )
        val fullQ4 = q4.copy(options = q4Options)

        val fullTask2 = task2.copy(questions = listOf(fullQ2))
        val fullTask3 = task3.copy(questions = listOf(fullQ3))
        val fullTask4 = task4.copy(questions = listOf(fullQ4))

        readingTaskRepository.saveAll(listOf(fullTask2, fullTask3, fullTask4))
    }
}
