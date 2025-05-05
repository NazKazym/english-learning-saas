package com.clbboost.repository.reading

import com.clbboost.domain.reading.ReadingOption
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface ReadingOptionRepository : JpaRepository<ReadingOption, UUID>