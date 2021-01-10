package com.maranin.kotlinfundemo.v3either

import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.maranin.kotlinfundemo.shared.*
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V3InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    fun getInvoiceForDay(date: LocalDate): Either<BadCalculation,InvoiceDay> {
        // Note the return value might be null
        val effort: DailyEffort? = dailyEffortsRepository.findByDate(date)
        // Note a problem is returned in case of null
        return effort?.let { calculateInvoice(it, date) } ?: Either.Left(NoEntryAvailableForDate(date))
    }

    private fun calculateInvoice(effort: DailyEffort, date: LocalDate): Either<BadCalculation,InvoiceDay> {
        return when {
            effort.hours >= 0 -> {
                val (amount, wage) = effort.calculateAmount()
                // Note the right side of Either captures the good case
                Right(InvoiceDay(date = date, hours = effort.hours, hourlyWage = wage, amount = amount))
            }
            // Note problems are communicated with the left side of Either
            else -> Left(InvalidEntry("A negative number of hours is not allowed!", effort))
        }
    }

}