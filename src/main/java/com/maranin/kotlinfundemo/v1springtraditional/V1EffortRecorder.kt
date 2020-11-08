package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.Effort
import com.maranin.kotlinfundemo.shared.Invoice
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.time.LocalDate

@Component
class V1EffortRecorder {

    private val hourlyWage = 5

    // TODO: implement JPA persistence
    private val map: MutableMap<LocalDate,Int> = HashMap()

    fun recordEffort(date: LocalDate, hours: Int): Effort {
        map[date] = hours
        return Effort(date, hours)
    }

    fun getInvoiceForDay(date: LocalDate): Invoice {
        val hours = map[date]?: throw RuntimeException("No efforts found for date $date")
        val amount = calculateAmountToInvoice(hours)
        return Invoice(from = date, to = date, hours = hours, hourlyWage = hourlyWage, amount = amount)
    }

    fun calculateAmountToInvoice(hours: Int) = hours * hourlyWage * 1.16

}