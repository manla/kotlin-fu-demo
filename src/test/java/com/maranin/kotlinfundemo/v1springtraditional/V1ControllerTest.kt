package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.Invoice
import com.maranin.kotlinfundemo.shared.InvoiceError
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import java.time.LocalDate

@SpringBootTest
internal class V1ControllerTest {

    @Autowired
    lateinit var controller: V1Controller


    private val date = LocalDate.of(2020, 11, 8)
    private val dateString = "2020-11-08"

    @BeforeEach
    fun clearEntries() {
        controller.deleteEfforts()
    }

    @Test
    fun recordEffort() {
        val (d, numberOfHours) = controller.recordEffort(dateString, 2)
        expectThat(d).isEqualTo(date)
        expectThat(numberOfHours).isEqualTo(2)
    }

    @Nested
    inner class GetInvoice {

        @Test
        fun getInvoiceForKnownDay() {
            controller.recordEffort(dateString, 2)
            val invoiceObject = controller.getInvoiceForDay(dateString)
            expectThat(invoiceObject).isA<Invoice>()
            val invoice = invoiceObject as Invoice
            expectThat(invoice.from).isEqualTo(date)
            expectThat(invoice.to).isEqualTo(date)
            expectThat(invoice.hours).isEqualTo(2)
            expectThat(invoice.amount).isEqualTo(11.6)
        }

        @Test
        fun getInvoiceFornegativeHours() {
            controller.recordEffort(dateString, -2)
            val invoiceObject = controller.getInvoiceForDay(dateString)
            expectThat(invoiceObject).isA<InvoiceError>()
        }

        @Test
        fun getInvoiceForUnknownDay() {
            val invoiceObject = controller.getInvoiceForDay(dateString)
            expectThat(invoiceObject).isA<Invoice>()
            val invoice = invoiceObject as Invoice
            expectThat(invoice.from).isEqualTo(date)
            expectThat(invoice.to).isEqualTo(date)
            expectThat(invoice.hours).isEqualTo(0)
            expectThat(invoice.amount).isEqualTo(0.0)
        }

    }
}