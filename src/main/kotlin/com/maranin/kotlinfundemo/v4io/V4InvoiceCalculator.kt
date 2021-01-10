package com.maranin.kotlinfundemo.v4io

import arrow.fx.IO
import arrow.fx.extensions.fx
import com.maranin.kotlinfundemo.shared.*
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V4InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    /**
     * Note IO's for-comprehension allows an imperative style instead of working with flatMap()
     * Note the
     * Note the increase of readability grows with the number of involved monads
     * '!' is a shortcut for .bind() at the end and provides an IO's content
     * Note there is no explicit check for null or for exceptions
     */
    fun getInvoiceForDayForComprehension(date: LocalDate): IO<InvoiceDay> = IO.fx {
        val effort: DailyEffort = !findByDateIO(date)
        val invoice: InvoiceDay = !calculateInvoice(effort, date)
        invoice
    }

    /**
     * An alternative variant using flatMap() instead of for-comprehension is provided for illustration.
     * The functionality is identical to that of function getInvoiceForDayForComprehension()
     */
    fun getInvoiceForDayFlatMap(date: LocalDate): IO<InvoiceDay> =
        findByDateIO(date).flatMap {
                effort -> calculateInvoice(effort, date)
        }

    /**
     * Note an IO monad is returned which handles exceptions
     * In the case of null, an exception is provoked explicitly
     */
    fun findByDateIO(date: LocalDate): IO<DailyEffort> =
        IO { dailyEffortsRepository.findByDate(date) ?: throw NoEntryAvailableException("No efforts available for date $date") }

    /**
     * Note an IO monad is returned which handles exceptions
     * In the case of null, an exception is provoked explicitly
     */
    private fun calculateInvoice(effort: DailyEffort, date: LocalDate): IO<InvoiceDay> =
        when {
            effort.hours >= 0 -> {
                val (amount, wage) = effort.calculateAmount()
                IO { InvoiceDay(date = date, hours = effort.hours, hourlyWage = wage, amount = amount) }
            }
            else -> throw InvalidEntryException("A negative number of hours is not allowed! $effort")
        }

}