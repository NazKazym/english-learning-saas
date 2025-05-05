package com.clbboost.service.reading

import com.clbboost.domain.reading.ReadingTask
import com.clbboost.dto.reading.ReadingOptionDTO
import com.clbboost.dto.reading.ReadingQuestionDTO
import com.clbboost.dto.reading.ReadingTaskDTO
import com.clbboost.repository.reading.ReadingTaskRepository
import org.springframework.stereotype.Service

@Service
class ReadingTaskService(
    private val readingTaskRepository: ReadingTaskRepository
) {

    fun findByPart(part: Int): List<ReadingTaskDTO> {
        val tasks: List<ReadingTask> = readingTaskRepository.findAllByPart(part)

        return tasks.map { task ->
            ReadingTaskDTO(
                id = task.id!!,
                title = task.title,
                part = task.part,
                level = task.level,
                passage = task.passage,
                diagramUrl = task.diagramUrl,
                questions = task.questions.map { question ->
                    ReadingQuestionDTO(
                        id = question.id!!,
                        questionText = question.questionText,
                        questionOrder = question.questionOrder,
                        options = question.options.map { option ->
                            ReadingOptionDTO(
                                id = option.id!!,
                                optionLabel = option.optionLabel,
                                optionText = option.optionText
                            )
                        }
                    )
                }
            )
        }
    }
}
