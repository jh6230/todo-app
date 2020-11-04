package model

import lib.model.Todo.TodoStatus

case class ViewValueTodo(
  head:       String,
  cssSrc:     Seq[String],
  jsSrc:      Seq[String],
  id:         Seq[Long],
  categoryId: Seq[Long],
  title:      Seq[String],
  content:    Seq[String],
  state:      Seq[TodoStatus]
)extends ViewValueCommon
