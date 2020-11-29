package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.DailyEffort
import com.maranin.kotlinfundemo.shared.DailyEffortsRepository
import com.maranin.kotlinfundemo.shared.Invoice
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V1InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    private val hourlyWage = 5

    fun getInvoiceForDay(date: LocalDate): Invoice? {
        // Note the return value might be null
        val effort: DailyEffort? = dailyEffortsRepository.findByDate(date)
        // Note the null check
        return if (effort == null)
            null
            else
            calculateInvoice(effort, date)
    }

    private fun calculateInvoice(effort: DailyEffort, date: LocalDate): Invoice {
        if (effort.hours >= 0) {
            val amount = calculateAmount(effort.hours)
            return Invoice(from = date, to = date, hours = effort.hours, hourlyWage = hourlyWage, amount = amount)
        } else {
            // Note an exception is thrown in case of negative hours
            throw java.lang.RuntimeException("A negative number of hours is not allowed!")
        }
    }

    fun calculateAmount(hours: Int) = hours * hourlyWage * 1.16

}