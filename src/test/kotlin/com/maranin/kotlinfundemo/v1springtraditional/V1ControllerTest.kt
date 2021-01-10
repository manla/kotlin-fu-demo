package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.BaseController
import com.maranin.kotlinfundemo.shared.InvoiceDay
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
    lateinit var baseController: BaseController

    @Autowired
    lateinit var controller: V1Controller


    private val date = LocalDate.of(2020, 11, 9)
    private val dateString = "2020-11-09"

    @BeforeEach
    fun clearEntries() {
        baseController.deleteEfforts()
    }

    @Test
    fun recordEffort() {
        val (d, numberOfHours) = baseController.recordEffort(dateString, 2)
        expectThat(d).isEqualTo(date)
        expectThat(numberOfHours).isEqualTo(2)
    }

    @Nested
    inner class GetInvoice {

        @Test
        fun getInvoiceForKnownDay() {
            baseController.recordEffort(dateString, 2)
            val invoiceObject = controller.getInvoiceForDay(dateString)
            expectThat(invoiceObject).isA<InvoiceDay>()
            val invoice = invoiceObject as InvoiceDay
            expectThat(invoice.date).isEqualTo(date)
            expectThat(invoice.hours).isEqualTo(2)
            expectThat(invoice.amount).isEqualTo(11.6)
        }

        @Test
        fun getInvoiceFornegativeHours() {
            baseController.recordEffort(dateString, -2)
            val invoiceObject = controller.getInvoiceForDay(dateString)
            expectThat(invoiceObject).isA<InvoiceError>()
        }

        @Test
        fun getInvoiceForUnknownDay() {
            val invoiceObject = controller.getInvoiceForDay(dateString)
            expectThat(invoiceObject).isA<String>()
        }

    }
}