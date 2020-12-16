package com.maranin.kotlinfundemo.shared

import java.time.LocalDate

sealed class InvoiceResponse

data class InvoiceError(val exception: String, val errorMessage: String?) : InvoiceResponse()

data class Invoice(val from: LocalDate, val to: LocalDate, val hours: Int, val hourlyWage: Int, val amount: Double) : InvoiceResponse()