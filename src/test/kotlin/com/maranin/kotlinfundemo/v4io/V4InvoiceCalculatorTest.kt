package com.maranin.kotlinfundemo.v4io

import arrow.core.Either
import arrow.fx.IO
import com.maranin.kotlinfundemo.shared.EffortRecorder
import com.maranin.kotlinfundemo.shared.*
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import strikt.api.expectThat
import strikt.assertions.isA
import strikt.assertions.isEqualTo
import strikt.assertions.isFalse
import java.time.LocalDate

@SpringBootTest
internal class V4InvoiceCalculatorTest {

    @Autowired
    lateinit var effortRecorder: EffortRecorder

    @Autowired
    lateinit var invoiceCalculator: V4InvoiceCalculator

    private val date = LocalDate.of(2020, 11, 8)

    @BeforeEach
    fun clearEntries() {
        effortRecorder.deleteEfforts()
    }

    // Note that IO monads have to be run explicitly, e.g. with unsafeRunSync(), in order to get the results checked.
    // If they are not run explicitely, nothing happens!

    @Test
    fun getInvoiceForCompForKnownDay() {
        effortRecorder.recordEffort(date, 2)
        val io: IO<InvoiceDay> = invoiceCalculator.getInvoiceForDayForComprehension(date)
        io.attempt().map {
            when (it) {
                is Either.Right -> {
                    val (date, hours, wage, amount) = it.b
                    expectThat(date).isEqualTo(this.date)
                    expectThat(hours).isEqualTo(2)
                    expectThat(wage).isEqualTo(10)
                    expectThat(amount).isEqualTo(23.2)
                }
                is Either.Left -> {
                    expectThat(true).isFalse() // report an error!
                }
            }
        }.unsafeRunSync()
    }

    @Test
    fun getInvoiceForCompForUnknownKnownDay() {
        val io: IO<InvoiceDay> = invoiceCalculator.getInvoiceForDayForComprehension(date)
        io.attempt().map {
            when (it) {
                is Either.Left -> {
                    val throwable = it.a
                    expectThat(throwable).isA<NoEntryAvailableException>()
                }
                is Either.Right -> {
                    expectThat(true).isFalse() // report an error!
                }
            }
        }.unsafeRunSync()
    }

    @Test
    fun getInvoiceFlatMapForKnownDay() {
        effortRecorder.recordEffort(date, 2)
        val io: IO<InvoiceDay> = invoiceCalculator.getInvoiceForDayFlatMap(date)
        io.attempt().map {
            when (it) {
                is Either.Right -> {
                    val (date, hours, wage, amount) = it.b
                    expectThat(date).isEqualTo(this.date)
                    expectThat(hours).isEqualTo(2)
                    expectThat(wage).isEqualTo(10)
                    expectThat(amount).isEqualTo(23.2)
                }
                is Either.Left -> {
                    expectThat(true).isFalse() // report an error!
                }
            }
        }.unsafeRunSync()
    }

    @Test
    fun getInvoiceFlatMapForUnknownKnownDay() {
        val io: IO<InvoiceDay> = invoiceCalculator.getInvoiceForDayFlatMap(date)
        io.attempt().map {
            when (it) {
                is Either.Left -> {
                    val throwable = it.a
                    expectThat(throwable).isA<NoEntryAvailableException>()
                }
                is Either.Right -> {
                    expectThat(true).isFalse() // report an error!
                }
            }
        }.unsafeRunSync()
    }

    @Test
    fun getInvoiceForNegativeHous() {
        effortRecorder.recordEffort(date, -2)
        val io: IO<InvoiceDay> = invoiceCalculator.getInvoiceForDayForComprehension(LocalDate.of(2020, 11, 8))
        io.attempt().map {
            when (it) {
                is Either.Left -> {
                    val throwable = it.a
                    expectThat(throwable).isA<InvalidEntryException>()
                }
                is Either.Right -> {
                    expectThat(true).isFalse() // report an error!
                }

            }
        }.unsafeRunSync()
    }

}