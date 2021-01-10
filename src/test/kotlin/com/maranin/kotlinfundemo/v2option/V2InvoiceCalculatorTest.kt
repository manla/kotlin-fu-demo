package com.maranin.kotlinfundemo.v2option

import arrow.core.Option
import arrow.core.extensions.option.foldable.isNotEmpty
import com.maranin.kotlinfundemo.shared.EffortRecorder
import com.maranin.kotlinfundemo.shared.InvoiceDay
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectCatching
import strikt.api.expectThat
import strikt.assertions.*
import java.lang.RuntimeException
import java.time.LocalDate

@SpringBootTest
internal class V2InvoiceCalculatorTest {

    @Autowired
    lateinit var effortRecorder: EffortRecorder

    @Autowired
    lateinit var invoiceCalculator: V2InvoiceCalculator

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
            val invoiceOption: Option<InvoiceDay> = invoiceCalculator.getInvoiceForDay(date)
            // Note the check for empty
            expectThat(invoiceOption.isNotEmpty()).isTrue()
            // Note the content is checked inside a map function
            invoiceOption.map { invoice -> {
                val (date, hours, _, amount) = invoice
                expectThat(date).isEqualTo(this@V2InvoiceCalculatorTest.date)
                expectThat(hours).isEqualTo(2)
                expectThat(amount).isEqualTo(11.6)
            } }
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
            val invoiceOption: Option<InvoiceDay> = invoiceCalculator.getInvoiceForDay(LocalDate.of(2020, 11, 8))
            expectThat(invoiceOption.isEmpty()).isTrue()
        }

    }

}