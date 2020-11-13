package com.maranin.kotlinfundemo.shared

import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class EffortRecorder(val dailyEffortsRepository: DailyEffortsRepository) {

    fun recordEffort(date: LocalDate, hours: Int): Effort {
        dailyEffortsRepository.save(DailyEffort(date, hours))
        return Effort(date, hours)
    }

    fun deleteEfforts() {
        dailyEffortsRepository.deleteAll()
    }

}