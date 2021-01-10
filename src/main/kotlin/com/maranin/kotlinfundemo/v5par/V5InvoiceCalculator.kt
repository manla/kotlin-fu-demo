package com.maranin.kotlinfundemo.v5par

import arrow.fx.IO
import arrow.fx.extensions.fx
import com.maranin.kotlinfundemo.shared.*
import com.maranin.kotlinfundemo.v4io.calculateInvoice
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class V5InvoiceCalculator(val dailyEffortsRepository: DailyEffortsRepository) {

    private val logger: Logger = LoggerFactory.getLogger(V5InvoiceCalculator::class.java)

    /**
     * Function parTraverse() is used for parallel processing, again in an IO monad.
     * For the sake of illustrating parallelism, the effort for each day is accessed
     * and processed for its own.
     */
    fun getInvoiceForPeriod(fromDate: LocalDate, toDate: LocalDate): IO<InvoicePeriod> {
        val dates: List<LocalDate> = dateListFromTo(fromDate, toDate)
        return IO.fx {
            val invoices: List<InvoiceDay> = !dates.parTraverse {
                getInvoiceForDay(it)
            }
            sumUp(fromDate, toDate, invoices)
        }
    }

    /**
     * The IO's for-comprehension allows an imperative style, as in version 4
     * Additionally the thread is logged to illustrate parallelism
     */
    fun getInvoiceForDay(date: LocalDate): IO<InvoiceDay> {
        logger.info("Get Invoice for $date with thread ${Thread.currentThread().name}")
        return IO.fx {
            val effort: DailyEffort = !findByDateIO(date)
            val invoice: InvoiceDay = !effort.calculateInvoice()
            invoice
        }
    }

    /**
     * Note an IO monad is returned which handles exceptions
     * In the case of null, a 0 Effort is returned to allow aggregation
     */
    fun findByDateIO(date: LocalDate): IO<DailyEffort> =
        IO { dailyEffortsRepository.findByDate(date) ?: DailyEffort(date, 0) }

    private fun dateListFromTo(fromDate: LocalDate, toDate: LocalDate): List<LocalDate> {
        tailrec fun go(from: LocalDate, to: LocalDate, dates: List<LocalDate>): List<LocalDate> =
            when {
                from.isAfter(to) -> dates
                from.isEqual(to) -> dates + from
                else -> go(from.plusDays(1), to, dates + from)
            }
        return go(fromDate, toDate, emptyList())
    }

    /**
     * aggregates the daily efforts
     */
    fun sumUp(fromDate: LocalDate, toDate: LocalDate, invoices: List<InvoiceDay>): InvoicePeriod {
        val relevantInvoices = invoices.filter { it.hours != 0 }
        val (hours: Int, amount: Double) = relevantInvoices.fold(
            Pair(0, 0.0),
            { pair, invoice -> Pair(pair.first + invoice.hours, pair.second + invoice.amount) }
        )
        return InvoicePeriod(fromDate, toDate, hours, amount, relevantInvoices)
    }

}