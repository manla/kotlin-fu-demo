package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.EffortRecorder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectCatching

import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import strikt.assertions.isNull
import java.lang.RuntimeException
import java.time.LocalDate

@SpringBootTest
internal class V1EffortRecorderTest {

    @Autowired
    lateinit var effortRecorder: EffortRecorder

    @Autowired
    lateinit var invoiceCalculator: V1InvoiceCalculator

    private val date = LocalDate.of(2020, 11, 8)

    @BeforeEach
    fun clearEntries() {
        effortRecorder.deleteEfforts()
    }

    @Nested
    inner class GetInvoiceTest {

        @Test
        fun getInvoiceForKnownDay() {
            effortRecorder.recordEffort(date, 2)
            val (from, to, hours, _, amount) = invoiceCalculator.getInvoiceForDay(date)!!
            expectThat(from).isEqualTo(date)
            expectThat(to).isEqualTo(date)
            expectThat(hours).isEqualTo(2)
            expectThat(amount).isEqualTo(11.6)
        }

        @Test
        fun getInvoiceForNegativeHous() {
            effortRecorder.recordEffort(date, -2)
            expectCatching { invoiceCalculator.getInvoiceForDay(LocalDate.of(2020, 11, 8)) }
                    .isFailure()
                    .isA<RuntimeException>()
        }

        @Test
        fun getInvoiceForUnknownDay() {
            val invoice = invoiceCalculator.getInvoiceForDay(LocalDate.of(2020, 11, 8))
            expectThat(invoice).isNull()
        }

    }

    @Test
    fun recordEffort() {
        val (dateReturned, numberOfHours) = effortRecorder.recordEffort(date, 2)
        expectThat(dateReturned).isEqualTo(date)
        expectThat(numberOfHours).isEqualTo(2)
    }

    @Test
    fun calculateAmountToInvoice() {
        val invoice = invoiceCalculator.calculateAmount(2)
        expectThat(invoice).isEqualTo(11.6)
    }

}