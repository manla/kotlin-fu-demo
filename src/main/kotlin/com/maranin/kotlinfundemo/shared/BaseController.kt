package com.maranin.kotlinfundemo.shared

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController()
@RequestMapping("/v0")
class BaseController(var effortRecorder: EffortRecorder) {

    private val logger: Logger = LoggerFactory.getLogger(BaseController::class.java)

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
     * clears recorded efforts
     */
    @DeleteMapping("/efforts")
    fun deleteEfforts(): String {
        logger.info("Delete all recorded efforts")
        effortRecorder.deleteEfforts()
        return "Recorded effords have been cleared"
    }

}
