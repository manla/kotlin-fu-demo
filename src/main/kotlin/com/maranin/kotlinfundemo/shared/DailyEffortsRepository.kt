package com.maranin.kotlinfundemo.shared

import org.springframework.data.repository.CrudRepository
import java.time.LocalDate

interface DailyEffortsRepository: CrudRepository<DailyEffort, LocalDate> {

    // Note the method might return null
    fun findByDate(date: LocalDate): DailyEffort?

}