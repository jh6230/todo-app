package model

import ixias.model._
import java.time.LocalDateTime

import lib.model.Todo
import lib.model.Todo.TodoStatus
import lib.model.Category
import lib.model.Category.CategoryColor
import lib.persistence.default.{ TodoRepository, CategoryRepository }
import lib.persistence.default.CategoryRepository
import controllers.todo.TodoForm
import play.api.data.Form
import play.api.data.Forms._

import Todo._

//todo.listで使用するカテゴリーの情報を持ったcase class
case class todoWithCategory(
  id: Option[Id],
  categoryId: Category.Id,
  categoryName: String,
  title:String,
  content:String,
  state: TodoStatus,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
)extends EntityModel[Id]


case class ViewValueTodo(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    todos: Seq[(TodoRepository.EntityEmbeddedId, Map[Category.Id, CategoryRepository.EntityEmbeddedId])]
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




