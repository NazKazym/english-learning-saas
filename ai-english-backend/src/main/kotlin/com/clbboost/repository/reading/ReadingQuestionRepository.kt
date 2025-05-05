package com.clbboost.repository.reading

import com.clbboost.domain.reading.ReadingQuestion
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReadingQuestionRepository : JpaRepository<ReadingQuestion, UUID>