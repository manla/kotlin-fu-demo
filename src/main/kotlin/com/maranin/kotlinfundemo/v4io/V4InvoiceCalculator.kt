package com.maranin.kotlinfundemo.v4io

import arrow.fx.IO
import arrow.fx.extensions.fx
import com.maranin.kotlinfundemo.shared.*
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V4InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    // Note an IO monad is returned which handles exceptions
    // In the case of null, an exception is provoked explicitly
    fun findByDateIO(date: LocalDate): IO<DailyEffort> =
        IO { dailyEffortsRepository.findByDate(date) ?: throw NoEntryAvailableException("No efforts available for date $date") }

    /**
     * IO's for-comprehension allows an imperative style instead of working with flatMap()
     * The '!' is a shortcut for .bind() at the end
     * Note there is no explicit check for null or for exceptions
     */
    fun getInvoiceForDay(date: LocalDate): IO<Invoice> = IO.fx {
        val effort: DailyEffort = !findByDateIO(date)
        val invoice: Invoice = !calculateInvoice(effort, date)
        invoice
    }

    private fun calculateInvoice(effort: DailyEffort, date: LocalDate): IO<Invoice> =
        when {
            effort.hours >= 0 -> {
                val (amount, wage) = effort.calculateAmount()
                // An IO monad is returned for the the good case
                IO { Invoice(from = date, to = date, hours = effort.hours, hourlyWage = wage, amount = amount) }
            }
            // An exception is thrown for the bad case
            else -> throw InvalidEntryException("A negative number of hours is not allowed! $effort")
        }

}