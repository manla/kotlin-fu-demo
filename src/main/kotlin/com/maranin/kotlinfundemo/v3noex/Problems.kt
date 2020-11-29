package com.maranin.kotlinfundemo.v3noex

import com.maranin.kotlinfundemo.shared.DailyEffort
import java.time.LocalDate

sealed class BadCalculation

data class InvalidEntry(val msg: String, val effort: DailyEffort): BadCalculation()

data class NoEntryAvailableForDate(val date: LocalDate): BadCalculation()