package model

import ixias.model._
import java.time.LocalDateTime

import lib.model.{ Todo, Category }
import lib.model.Todo.TodoStatus
import lib.model.Category
import lib.model.Category.CategoryColor
import lib.persistence.default.{ TodoRepository, CategoryRepository }
import lib.persistence.default.CategoryRepository
import controllers.todo.TodoForm
import play.api.data.Form
import play.api.data.Forms._


//todo.listで使用するカテゴリーの情報を持ったcase class
case class TodoWithCategory(
  id:       Todo.Id,
  categoryId: Category.Id,
  title: String,
  content: String,
  state: TodoStatus,
  updatedAt: LocalDateTime,
  categoryName:Option[String],
  categoryColor:Option[CategoryColor]
)


case class ViewValueTodo(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    todos: Seq[TodoWithCategory]
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




