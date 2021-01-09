package com.maranin.kotlinfundemo.v5par

import arrow.core.Either
import arrow.core.Either.Left
import arrow.core.Either.Right
import arrow.fx.IO
import arrow.fx.extensions.fx
import com.maranin.kotlinfundemo.shared.Invoice
import com.maranin.kotlinfundemo.shared.InvoiceError
import com.maranin.kotlinfundemo.shared.InvoiceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController()
@RequestMapping("/v5")
class V5Controller(var invoiceCalculator: V5InvoiceCalculator) {

    private val logger: Logger = LoggerFactory.getLogger(V5Controller::class.java)

    // Todo: Adjust doc

    /**
     * Indicates amount to invoice for a given day
     * Note the invoice calculation is provided wrapped in an IO monad
     * IO monads are designed to encapsulate calculations with side effects.
     * They provide support for error handling, async execution and parallel execution.
     * An IO monad only describes functionality. It has to be run explicitly.
     * Function unsafeRunSync() runs the monad. The name expresses that calculations might contain side effects
     * Null case are handled by the IO monad as well as long as they provoke exceptions
     * Function 'attempt()' generates an IO<Either<Throwable, Invoice>> to distinguish invalid and valid results
     * Note we do _not_ follow here the convention to declare functions containing side effects as 'suspend'.
     * This is because 'suspend' generates an additional Continuation parameter which would conflict here with our Spring endpoint method.
     * A similar conflict would arise with Spring Data Repository methods
     */
    @GetMapping("/invoices")
    fun getInvoiceForDay(@RequestParam(value = "from") fromDateString: String,
                         @RequestParam(value = "to") toDateString: String): InvoiceResponse {
        // Todo: Adjust method
        // Todo: adjust return type
        val invoiceForDayIo: IO<Invoice> = getInvoiceForPeriodIo(fromDateString, toDateString)
        val invoiceEither: IO<Either<Throwable, Invoice>> = invoiceForDayIo.attempt()
        return invoiceEither.map {
            when (it) {
                is Left -> {
                    logger.error("An exception occured: ${it.a}")
                    InvoiceError(it.a::class.java.simpleName, it.a.message)
                }
                is Right -> it.b
            }
        }.unsafeRunSync()
    }

    // Todo: Adjust doc
    /**
     * IO's for-comprehension IO.fx allows an imperative style instead of working with flatMap()
     * The '!' is a shortcut for .bind() at the end and provides an IO's content
     * Note there is no explicit check for null or for exceptions
     */
    fun getInvoiceForPeriodIo(fromDateString: String, toDateString: String): IO<Invoice> = IO.fx {
        // Todo: Adjust method
        logger.info("Retrieve invoice for period from $fromDateString to $toDateString")
        val fromDate: LocalDate = !parseDate(fromDateString)
        val toDate: LocalDate = !parseDate(toDateString)
        val invoice: Invoice = !invoiceCalculator.getInvoiceForPeriod(fromDate, toDate)
        invoice
    }

    // Todo: Reuse method
    /**
     * The parsing is wrapped into an IO monad to get error handling
     */
    private fun parseDate(dateString: String): IO<LocalDate> =
        IO { LocalDate.parse(dateString) }

}
