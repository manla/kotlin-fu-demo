package com.maranin.kotlinfundemo.v3noex

import arrow.core.Either
import com.maranin.kotlinfundemo.shared.Invoice
import com.maranin.kotlinfundemo.shared.InvoiceError
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController()
@RequestMapping("/v3")
class V3Controller(var invoiceCalculator: V3InvoiceCalculator) {

    private val logger: Logger = LoggerFactory.getLogger(V3Controller::class.java)

    /**
     * Indicates amount to invoice for a given day
     */
    @GetMapping("/invoices/{date}")
    fun getInvoiceForDay(@PathVariable(value = "date") dateString: String): Any {
        logger.info("Retrieve invoice for $dateString")
        val localDate: LocalDate = LocalDate.parse(dateString)
        val result: Either<BadCalculation,Invoice> = invoiceCalculator.getInvoiceForDay(localDate)
        // Note the null case is modelled as a problem as well, so all cases are handled with Either
        return when (result) {
            is Either.Left -> {
                logger.error("An exception occured: ${result.a}")
                InvoiceError(result.a::class.java.toString(), result.a.toString())
            }
            is Either.Right -> result.b
        }
    }

}
