package model

import lib.model.Todo
import lib.model.Todo.TodoStatus
import lib.model.Category
import lib.model.Category.CategoryColor
import lib.persistence.default.TodoRepository.EntityEmbeddedId
import controllers.todo.TodoForm
import play.api.data.Form
import play.api.data.Forms._

case class ViewValueTodo(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    todos: Seq[(EntityEmbeddedId, Map[Category.Id, Category])]
               //todo
) extends ViewValueCommon

case class ViewValueTodoAdd(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    todoForm: Form[TodoForm],
    categories: Seq[(String, String)]
) extends ViewValueCommon

case class ViewValueTodoEdit(
    id:   Long, 
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    todoForm: Form[TodoForm],
    categories: Seq[(String, String)]
) extends ViewValueCommon




