package model

import lib.model.Todo
import lib.model.Todo.TodoStatus

case class ViewValueTodo(
  head:       String,
  cssSrc:     Seq[String],
  jsSrc:      Seq[String],
  todo:       Seq[Todo]
)extends ViewValueCommon

