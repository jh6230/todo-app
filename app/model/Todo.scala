package model

import lib.model.Todo
import lib.model.Todo.TodoStatus
import lib.model.Category
import lib.model.Category.CategoryColor
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import controllers.todo.TodoForm
import play.api.data.Form
import play.api.data.Forms._

case class ViewValueTodo(
    head:     String,
    cssSrc:   Seq[String],
    jsSrc:    Seq[String],
    todo:     Seq[(Todo, Map[Category.Id, Category])] 
) extends ViewValueCommon

case class ViewValueTodoForm(
    head:     String,
    cssSrc:   Seq[String],
    jsSrc:    Seq[String],
    todoForm: Form[TodoForm]
) extends ViewValueCommon
