package com.maranin.kotlinfundemo.shared

import java.time.LocalDate

data class InvoiceError(val exception: String, val errorMessage: String?)

data class Effort(val date: LocalDate, val numberOfHours: Int)

data class Invoice(val from: LocalDate, val to: LocalDate, val hours: Int, val hourlyWage: Int, val amount: Double)