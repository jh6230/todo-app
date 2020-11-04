package model

case class ViewValueTodo(
  head:       String,
  cssSrc:     Seq[String],
  jsSrc:      Seq[String],
  id:         Seq[Long],
  categoryId: Seq[Long],
  title:      Seq[String],
  content:    Seq[String]
)extends ViewValueCommon
