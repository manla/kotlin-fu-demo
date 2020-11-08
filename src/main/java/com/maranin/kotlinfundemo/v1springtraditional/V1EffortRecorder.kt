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
        val optionlEffort = dailyEffortsRepository.findById(date)
        if (optionlEffort.isPresent) {
            val effort = optionlEffort.get()
            val amount = calculateAmountToInvoice(effort.hours)
            return Invoice(from = date, to = date, hours = effort.hours, hourlyWage = hourlyWage, amount = amount)
        } else {
            throw RuntimeException("No efforts found for date $date")
        }
    }

    fun calculateAmountToInvoice(hours: Int) = hours * hourlyWage * 1.16

    fun clearEntries() {
        dailyEffortsRepository.deleteAll()
    }

}