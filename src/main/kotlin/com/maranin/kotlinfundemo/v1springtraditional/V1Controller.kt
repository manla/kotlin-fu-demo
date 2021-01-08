package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.Invoice
import com.maranin.kotlinfundemo.shared.InvoiceError
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController()
@RequestMapping("/v1")
class V1Controller(var invoiceCalculator: V1InvoiceCalculator) {

    private val logger: Logger = LoggerFactory.getLogger(V1Controller::class.java)

    /**
     * Indicates amount to invoice for a given day
     */
    @GetMapping("/invoices/{date}")
    fun getInvoiceForDay(@PathVariable(value = "date") dateString: String): Any {
        // Note the return type is not precise because of the possible error case
        // Note exceptions are handled by try / catch clauses
        try {
            logger.info("Retrieve invoice for $dateString")
            val localDate: LocalDate = LocalDate.parse(dateString)
            val invoice: Invoice? = invoiceCalculator.getInvoiceForDay(localDate)
            // Note the null check with the handy Elvis operator
            return invoice?: "No efforts available for $dateString"
        } catch(e: RuntimeException) {
            logger.error("An exception occured: ${e::class.java}, ${e.message}")
            return InvoiceError(e::class.java.simpleName, e.message)
        }
    }

}
