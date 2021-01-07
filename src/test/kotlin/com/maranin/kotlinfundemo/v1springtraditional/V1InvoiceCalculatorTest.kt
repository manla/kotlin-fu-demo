package com.maranin.kotlinfundemo.v1springtraditional

import com.maranin.kotlinfundemo.shared.DailyEffort
import com.maranin.kotlinfundemo.shared.EffortRecorder
import com.maranin.kotlinfundemo.shared.calculateAmount
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

    private val date = LocalDate.of(2020, 11, 9)

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
        fun getInvoiceForNegativeHours() {
            effortRecorder.recordEffort(date, -2)
            expectCatching { invoiceCalculator.getInvoiceForDay(LocalDate.of(2020, 11, 9)) }
                    .isFailure()
                    .isA<RuntimeException>()
        }

        @Test
        fun getInvoiceForUnknownDay() {
            val invoice = invoiceCalculator.getInvoiceForDay(LocalDate.of(2020, 11, 9))
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
    fun calculateAmountWorkday() {
        val (amount, wage) = DailyEffort(LocalDate.of(2021, 1, 1), 2).calculateAmount()
        expectThat(amount).isEqualTo(11.6)
        expectThat(wage).isEqualTo(5)
    }

    @Test
    fun calculateAmountWeekend() {
        val (amount, wage) = DailyEffort(LocalDate.of(2021, 1, 2), 2).calculateAmount()
        expectThat(amount).isEqualTo(23.2)
        expectThat(wage).isEqualTo(10)
    }


}