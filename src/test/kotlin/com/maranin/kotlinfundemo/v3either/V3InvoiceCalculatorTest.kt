package com.maranin.kotlinfundemo.v3either

import arrow.core.Either
import com.maranin.kotlinfundemo.shared.EffortRecorder
import com.maranin.kotlinfundemo.shared.Invoice
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import strikt.assertions.isTrue
import java.time.LocalDate

@SpringBootTest
internal class V3InvoiceCalculatorTest {

    @Autowired
    lateinit var effortRecorder: EffortRecorder

    @Autowired
    lateinit var invoiceCalculator: V3InvoiceCalculator

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
            val result: Either<BadCalculation,Invoice> = invoiceCalculator.getInvoiceForDay(date)
            // Note the check for right
            expectThat(result.isRight()).isTrue()
            // Note the content is checked inside a map function. No difference to Option variant
            result.map { invoice -> {
                val (from, to, hours, _, amount) = invoice
                expectThat(from).isEqualTo(date)
                expectThat(to).isEqualTo(date)
                expectThat(hours).isEqualTo(2)
                expectThat(amount).isEqualTo(11.6)
            } }
        }

        @Test
        fun getInvoiceForNegativeHous() {
            effortRecorder.recordEffort(date, -2)
            val result: Either<BadCalculation,Invoice> = invoiceCalculator.getInvoiceForDay(LocalDate.of(2020, 11, 8))
            expectThat(result.isLeft()).isTrue()
            val left = result as  Either.Left
            expectThat(left.a is InvalidEntry)
        }

        @Test
        fun getInvoiceForUnknownDay() {
            val result: Either<BadCalculation,Invoice> = invoiceCalculator.getInvoiceForDay(LocalDate.of(2020, 11, 8))
            expectThat(result.isLeft()).isTrue()
        }

    }

    @Test
    fun calculateAmountToInvoice() {
        val invoice = invoiceCalculator.calculateAmount(2)
        expectThat(invoice).isEqualTo(11.6)
    }
}