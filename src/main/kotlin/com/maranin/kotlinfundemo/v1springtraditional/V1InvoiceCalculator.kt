package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.*
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V1InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    fun getInvoiceForDay(date: LocalDate): InvoiceDay? {
        // Note the return value might be null
        val effort: DailyEffort? = dailyEffortsRepository.findByDate(date)
        // Note the implicit null handling of let
        return effort?.let { calculateInvoice(it, date) }
    }

    private fun calculateInvoice(effort: DailyEffort, date: LocalDate): InvoiceDay {
        if (effort.hours >= 0) {
            val (amount, wage) = effort.calculateAmount()
            return InvoiceDay(date = date, hours = effort.hours, hourlyWage = wage, amount = amount)
        } else {
            // Note an exception is thrown in case of negative hours
            throw java.lang.RuntimeException("A negative number of hours is not allowed!")
        }
    }

}