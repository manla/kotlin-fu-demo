package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.Effort
import com.maranin.kotlinfundemo.shared.InvoiceError
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController()
@RequestMapping("/v1")
class V1Controller(var effortRecorder: V1EffortRecorder) {

    private val logger: Logger = LoggerFactory.getLogger(V1Controller::class.java)

    /**
     * Record number of hours worked on a day
     */
    @PostMapping("/efforts/{date}")
    fun recordEffort(@PathVariable(value = "date") dateString: String, @RequestParam(value = "hours") hours: Int): Effort {
        logger.info("Record an effort of $hours hours for date $dateString")
        val date: LocalDate = LocalDate.parse(dateString)
        return effortRecorder.recordEffort(date, hours)
    }

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
            return effortRecorder.getInvoiceForDay(localDate)
        } catch(e: RuntimeException) {
            logger.error("An exception occured: ${e::class.java}, ${e.message}")
            return InvoiceError(e::class.java.toString(), e.message)
        }
    }

}
