package com.clbboost.repository.reading

import com.clbboost.domain.reading.ReadingTask
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReadingTaskRepository : JpaRepository<ReadingTask, UUID> {
    fun findAllByPart(part: Int): List<ReadingTask>
}