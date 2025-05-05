package com.clbboost.controller.reading

import com.clbboost.dto.reading.ReadingTaskDTO
import com.clbboost.service.reading.ReadingTaskService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/reading-tasks")
class ReadingTaskController(private val readingTaskService: ReadingTaskService) {

    @GetMapping("/part/{partNumber}")
    fun getTasksByPart(@PathVariable partNumber: Int): List<ReadingTaskDTO> =
        readingTaskService.findByPart(partNumber)
}
