package com.maranin.kotlinfundemo.v1springtraditional

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import strikt.api.expectCatching

import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFailure
import java.lang.RuntimeException
import java.time.LocalDate

internal class V1EffortRecorderTest {

    private val date = LocalDate.of(2020, 11, 8)

    @Nested
    inner class GetInvoiceTest {

        @Test
        fun getInvoiceForUnknownDay() {
            val effortRecorder = V1EffortRecorder()
            expectCatching { effortRecorder.getInvoiceForDay(LocalDate.of(2020, 11, 8)) }
                    .isFailure()
                    .isA<RuntimeException>()
        }

        @Test
        fun getInvoiceForKnownDay() {
            val effortRecorder = V1EffortRecorder()
            effortRecorder.recordEffort(date, 2)
            val (from, to, hours, _, amount) = effortRecorder.getInvoiceForDay(date)
            expectThat(from).isEqualTo(date)
            expectThat(to).isEqualTo(date)
            expectThat(hours).isEqualTo(2)
            expectThat(amount).isEqualTo(11.6)
        }

    }

    @Test
    fun recordEffort() {
        val effortRecorder = V1EffortRecorder()
        val (dateReturned, numberOfHours) = effortRecorder.recordEffort(date, 2)
        expectThat(dateReturned).isEqualTo(date)
        expectThat(numberOfHours).isEqualTo(2)
    }

    @Test
    fun calculateAmountToInvoice() {
        val effortRecorder = V1EffortRecorder()
        val invoice = effortRecorder.calculateAmountToInvoice(2)
        expectThat(invoice).isEqualTo(11.6)
    }

}