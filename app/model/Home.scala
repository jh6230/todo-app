package model

// Topページのviewvalue
case class ViewValueHome(
  head:  String,
  cssSrc: Seq[String],
  jsSrc:  Seq[String],
) extends ViewValueCommon

