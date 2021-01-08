package com.maranin.kotlinfundemo.v2option

import arrow.core.Option
import arrow.core.getOrElse
import com.maranin.kotlinfundemo.shared.Invoice
import com.maranin.kotlinfundemo.shared.InvoiceError
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController()
@RequestMapping("/v2")
class V2Controller(var invoiceCalculator: V2InvoiceCalculator) {

    private val logger: Logger = LoggerFactory.getLogger(V2Controller::class.java)

    /**
     * Indicates amount to invoice for a given day
     */
    @GetMapping("/invoices/{date}")
    fun getInvoiceForDay(@PathVariable(value = "date") dateString: String): Any {
        try {
            logger.info("Retrieve invoice for $dateString, with arrow option")
            val localDate: LocalDate = LocalDate.parse(dateString)
            val invoiceOption: Option<Invoice> = invoiceCalculator.getInvoiceForDay(localDate)
            // Note the optio check with the handy getOrElse method
            return invoiceOption.getOrElse { "No efforts available for $dateString" }
        } catch(e: RuntimeException) {
            logger.error("An exception occured: ${e::class.java.simpleName}, ${e.message}")
            return InvoiceError(e::class.java.toString(), e.message)
        }
    }

}
