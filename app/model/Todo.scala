package model

import lib.model.Todo
import lib.model.Todo.TodoStatus
import controllers.todo.TodoForm
import play.api.data.Form
import play.api.data.Forms._

case class ViewValueTodo(
  head:       String,
  cssSrc:     Seq[String],
  jsSrc:      Seq[String],
  todo:       Seq[Todo]
)extends ViewValueCommon


case class ViewValueTodoForm(
  head:       String,
  cssSrc:     Seq[String],
  jsSrc:      Seq[String],
  todoForm:   Form[TodoForm]
)extends ViewValueCommon
