package com.maranin.kotlinfundemo.shared

import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface DailyEffortsRepository: CrudRepository<DailyEffort, LocalDate>