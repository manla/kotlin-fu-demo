package com.maranin.kotlinfundemo.v3noex

import arrow.core.Either
import arrow.core.Right
import com.maranin.kotlinfundemo.shared.DailyEffort
import com.maranin.kotlinfundemo.shared.DailyEffortsRepository
import com.maranin.kotlinfundemo.shared.Invoice
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V3InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    private val hourlyWage = 5

    fun getInvoiceForDay(date: LocalDate): Either<BadCalculation,Invoice> {
        // Note the return value might be null
        val effort: DailyEffort? = dailyEffortsRepository.findByDate(date)
        // Note a problem is returned in case of null
        return effort?.let { calculateInvoice(it, date) } ?: Either.Left(NoEntryAvailableForDate(date))
    }

    private fun calculateInvoice(effort: DailyEffort, date: LocalDate): Either<BadCalculation,Invoice> {
        return when {
            effort.hours >= 0 -> {
                val amount = calculateAmount(effort.hours)
                Right(Invoice(from = date, to = date, hours = effort.hours, hourlyWage = hourlyWage, amount = amount))
            }
            // Note a problem is returned in case of negative hours
            else -> Either.Left(InvalidEntry("A negative number of hours is not allowed!", effort))
        }
    }

    fun calculateAmount(hours: Int) = hours * hourlyWage * 1.16

}