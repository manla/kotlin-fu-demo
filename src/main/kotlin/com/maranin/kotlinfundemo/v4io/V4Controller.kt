package com.maranin.kotlinfundemo.v4io

import arrow.core.Either.Left
import arrow.core.Either.Right
import com.maranin.kotlinfundemo.shared.InvoiceError
import com.maranin.kotlinfundemo.shared.InvoiceResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController()
@RequestMapping("/v4")
class V4Controller(var invoiceCalculator: V4InvoiceCalculator) {

    private val logger: Logger = LoggerFactory.getLogger(V4Controller::class.java)

    /**
     * Indicates amount to invoice for a given day
     */
    @GetMapping("/invoices/{date}")
    fun getInvoiceForDay(@PathVariable(value = "date") dateString: String): InvoiceResponse {
        logger.info("Retrieve invoice for $dateString")
        val localDate: LocalDate = LocalDate.parse(dateString)
        return getInvoiceForDay(localDate)
    }

    /**
     * Note a monad only describes functionality and has to be run explicitly
     * Note the null case is handled by the IO monad as well, because it provokes an exception
     * Note 'attempt()' generates an Either<Throwable, Invoice> out of the IO monad
     */
    // Todo: polish code
    // Todo: document code
    // Todo: answer open questions: What's with suspend / suspended??? What's with unsafeRunSync() ???
    // Todo: Wrap parsing in IO as well?
    private fun getInvoiceForDay(localDate: LocalDate): InvoiceResponse =
        invoiceCalculator.getInvoiceForDay(localDate).attempt().map {
            when (it) {
                is Left -> {
                    logger.error("An exception occured: ${it.a}")
                    InvoiceError(it.a::class.java.toString(), it.a.toString())
                }
                is Right -> it.b
            }
        }.unsafeRunSync()

}
