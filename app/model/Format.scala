package model

import java.time.LocalDateTime
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object Format {
  def localDataTime(time: LocalDateTime): String = {
    time.format(DateTimeFormatter.ofPattern("yyy年MM月dd日"))
  }

  def localDate(time: LocalDate): String = {
    time.format(DateTimeFormatter.ofPattern("yyy年MM月dd日"))
  }

}
