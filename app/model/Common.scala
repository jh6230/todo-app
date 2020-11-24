package model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

trait ViewValueCommon {
  val head: String
  val cssSrc: Seq[String]
  val jsSrc: Seq[String]
}

object Format{
  def time(time: LocalDateTime):String = {
    time.format(DateTimeFormatter.ofPattern("yyy年MM月dd日"))
  }

}
