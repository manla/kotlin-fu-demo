package com.maranin.kotlinfundemo.v5par

import arrow.fx.IO
import arrow.fx.extensions.fx
import com.maranin.kotlinfundemo.shared.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V5InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    private val logger: Logger = LoggerFactory.getLogger(V5InvoiceCalculator::class.java)

    // Todo: document method
    fun getInvoiceForPeriod(fromDate: LocalDate, toDate: LocalDate): IO<Invoice> {
        val dates: List<LocalDate> = dateListFromTo(fromDate, toDate)
        return IO.fx {
            val invoices: List<Invoice> = !dates.parTraverse {
                getInvoiceForDay(it)
            }
            sumUp(invoices)
        }
    }

    // Todo: Adjust doc
    /**
     * Note IO's for-comprehension allows an imperative style instead of working with flatMap()
     * Note the
     * Note the increase of readability grows with the number of involved monads
     * '!' is a shortcut for .bind() at the end and provides an IO's content
     * Note there is no explicit check for null or for exceptions
     */
    // Todo: Adjust or reuse method
    fun getInvoiceForDay(date: LocalDate): IO<Invoice> {
        logger.info("Get Invoice for $date with thread ${Thread.currentThread().name}")
        return IO.fx {
            val effort: DailyEffort = !findByDateIO(date)
            val invoice: Invoice = !calculateInvoice(effort, date)
            invoice
        }
    }

    /**
     * Note an IO monad is returned which handles exceptions
     * In the case of null, a 0 Effort is returned
     */
    fun findByDateIO(date: LocalDate): IO<DailyEffort> =
        IO { dailyEffortsRepository.findByDate(date) ?: DailyEffort(date, 0) }

    // Todo: Reuse method
    /**
     * Note an IO monad is returned which handles exceptions
     * In the case of null, an exception is provoked explicitly
     */
    private fun calculateInvoice(effort: DailyEffort, date: LocalDate): IO<Invoice> =
        when {
            effort.hours >= 0 -> {
                val (amount, wage) = effort.calculateAmount()
                IO { Invoice(from = date, to = date, hours = effort.hours, hourlyWage = wage, amount = amount) }
            }
            else -> throw InvalidEntryException("A negative number of hours is not allowed! $effort")
        }

    private fun dateListFromTo(fromDate: LocalDate, toDate: LocalDate): List<LocalDate> {
        tailrec fun go(from: LocalDate, to: LocalDate, dates: List<LocalDate>): List<LocalDate> =
            when {
                from.isAfter(to) -> dates
                from.isEqual(to) -> dates + from
                else -> go(from.plusDays(1), to, dates + from)
            }
        return go(fromDate, toDate, emptyList())
    }

    // Todo: implement function
    fun sumUp(invoices: List<Invoice>): Invoice =
        Invoice(LocalDate.now(), LocalDate.now(), 42, 4, 4.2)

}