package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.DailyEffort
import com.maranin.kotlinfundemo.shared.DailyEffortsRepository
import com.maranin.kotlinfundemo.shared.Effort
import com.maranin.kotlinfundemo.shared.Invoice
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V1EffortRecorder(val dailyEffortsRepository: DailyEffortsRepository) {

    private val hourlyWage = 5

    fun recordEffort(date: LocalDate, hours: Int): Effort {
        dailyEffortsRepository.save(DailyEffort(date, hours))
        return Effort(date, hours)
    }

    fun getInvoiceForDay(date: LocalDate): Invoice {
        val effort: DailyEffort? = dailyEffortsRepository.findByDate(date)
        // Note the traditional null check
        return if (effort == null)
            Invoice(date, date, 0, hourlyWage, 0.0)
            else
            calculateInvoice(effort, date)
    }

    private fun calculateInvoice(effort: DailyEffort, date: LocalDate): Invoice {
        if (effort.hours >= 0) {
            val amount = calculateAmount(effort.hours)
            return Invoice(from = date, to = date, hours = effort.hours, hourlyWage = hourlyWage, amount = amount)
        } else {
            throw java.lang.RuntimeException("A negative number of hours is not allowed!")
        }
    }

    fun calculateAmount(hours: Int) = hours * hourlyWage * 1.16

    fun deleteEfforts() {
        dailyEffortsRepository.deleteAll()
    }

}