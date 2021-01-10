package com.maranin.kotlinfundemo.v5par

import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.fx.IO
import arrow.fx.extensions.fx
import com.maranin.kotlinfundemo.shared.InvoiceError
import com.maranin.kotlinfundemo.shared.InvoicePeriod
import com.maranin.kotlinfundemo.shared.InvoiceResponse
import com.maranin.kotlinfundemo.v4io.parseDateIo
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController()
@RequestMapping("/v5")
class V5Controller(var invoiceCalculator: V5InvoiceCalculator) {

    private val logger: Logger = LoggerFactory.getLogger(V5Controller::class.java)

    /**
     * Indicates amount to invoice for a given time period.
     * The time period allows to illustrate parallel processing of the single days.
     * Note the invoice calculation is provided wrapped in an IO monad again, as in variant 4.
     * The processing of the IO is written a bit more compact here as a single expression.
     */
    @GetMapping("/invoices")
    fun getInvoiceForDay(
        @RequestParam(value = "from") fromDateString: String,
        @RequestParam(value = "to") toDateString: String
    ): InvoiceResponse =
        getInvoiceForPeriodIo(fromDateString, toDateString)
            .attempt()
            .map {
                when (it) {
                    is Left -> {
                        logger.error("An exception occured: ${it.a}")
                        InvoiceError(it.a::class.java.simpleName, it.a.message)
                    }
                    is Right -> it.b
                }
            }.unsafeRunSync()


    /**
     * Provides the Invoice for the period as an IO monad
     */
    fun getInvoiceForPeriodIo(fromDateString: String, toDateString: String): IO<InvoicePeriod> = IO.fx {
        logger.info("Retrieve invoice for period from $fromDateString to $toDateString")
        val fromDate: LocalDate = !parseDateIo(fromDateString)
        val toDate: LocalDate = !parseDateIo(toDateString)
        val invoice: InvoicePeriod = !invoiceCalculator.getInvoiceForPeriod(fromDate, toDate)
        invoice
    }

}
