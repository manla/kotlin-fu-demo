package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.Invoice
import com.maranin.kotlinfundemo.shared.InvoiceError
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.time.LocalDate

internal class V1ControllerTest {

    private val date = LocalDate.of(2020, 11, 8)
    private val dateString = "2020-11-08"

    @Test
    fun recordEffort() {
     val controller = V1Controller(V1EffortRecorder())
        val (d, numberOfHours) = controller.recordEffort(dateString, 2)
        expectThat(d).isEqualTo(date)
        expectThat(numberOfHours).isEqualTo(2)
    }

    @Nested
    inner class GetInvoice {

        @Test
        fun getInvoiceForUnknownDay() {
            val controller = V1Controller(V1EffortRecorder())
            val invoiceObject = controller.getInvoiceForDay(dateString)
            expectThat(invoiceObject).isA<InvoiceError>()
        }

        @Test
        fun getInvoiceForKnownDay() {
            val controller = V1Controller(V1EffortRecorder())
            controller.recordEffort(dateString, 2)
            val invoiceObject = controller.getInvoiceForDay(dateString)
            expectThat(invoiceObject).isA<Invoice>()
            val invoice = invoiceObject as Invoice
            expectThat(invoice.from).isEqualTo(date)
            expectThat(invoice.to).isEqualTo(date)
            expectThat(invoice.hours).isEqualTo(2)
            expectThat(invoice.amount).isEqualTo(11.6)
        }

    }
}