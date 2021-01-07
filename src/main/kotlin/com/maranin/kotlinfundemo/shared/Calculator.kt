package com.maranin.kotlinfundemo.shared

import java.time.LocalDate

private const val hourlyWageWorkingDay = 5

private const val hourlyWageWeekend = 10

fun DailyEffort.calculateAmount(): Pair<Double, Int> =
    date.getWage().let { Pair(hours * it * 1.16, it) }

fun LocalDate.getWage(): Int =
    if (dayOfWeek.value <= 5) hourlyWageWorkingDay else hourlyWageWeekend
