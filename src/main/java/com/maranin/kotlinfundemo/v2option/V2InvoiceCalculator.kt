package com.maranin.kotlinfundemo.v2option

import arrow.core.Option
import com.maranin.kotlinfundemo.shared.DailyEffort
import com.maranin.kotlinfundemo.shared.DailyEffortsRepository
import com.maranin.kotlinfundemo.shared.Effort
import com.maranin.kotlinfundemo.shared.Invoice
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V2InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    private val hourlyWage = 5

    /* Note that option implements a functional pattern (It's a monad).
     * Since Kotlin deals with null values in it's own built in way,
     * Arrow recommends using Kotlin's language features and has deprecated arrow options.
     */

    fun getInvoiceForDay(date: LocalDate): Option<Invoice> {
        // Note the return value might be null
        val effortNullable: DailyEffort? = dailyEffortsRepository.findByDate(date)
        // Note the null check is handled implicitly
        val effortOption: Option<DailyEffort> = Option.fromNullable(effortNullable)
        return effortOption.map { effort -> calculateInvoice(effort, date) }
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