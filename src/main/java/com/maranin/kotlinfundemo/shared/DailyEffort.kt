package com.maranin.kotlinfundemo.shared

import java.time.LocalDate
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class DailyEffort(@Id var date: LocalDate, var hours: Int)