package model

import model.component.ViewValueUser

trait ViewValueCommon {
  val head: String
  val cssSrc: Seq[String]
  val jsSrc: Seq[String]
  val user: Option[ViewValueUser] = None
}
