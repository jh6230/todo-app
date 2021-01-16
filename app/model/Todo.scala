package model

import java.time.LocalDateTime
import java.time.LocalDate

import lib.model.{Todo, Category}
import lib.model.Todo.TodoStatus
import lib.model.Category.CategoryColor
import controllers.todo.TodoForm
import play.api.data.Form
import model.component.ViewValueUser

//todo.listで使用するカテゴリーの情報を持ったcase class
case class TodoWithCategory(
    id: Todo.Id,
    categoryId: Category.Id,
    title: String,
    content: String,
    state: TodoStatus,
    deadline: LocalDate,
    updatedAt: LocalDateTime,
    categoryName: Option[String],
    categoryColor: Option[CategoryColor]
)

case class ViewValueTodo(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    override val user: Option[ViewValueUser],
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
    id: Long,
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    todoForm: Form[TodoForm],
    categories: Seq[(String, String)]
) extends ViewValueCommon
