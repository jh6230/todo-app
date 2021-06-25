package model.JsValue

import play.api.libs.json._
import lib.model.{Todo, Category}
import play.api.data.Form
import play.api.data.Forms._
import java.time.LocalDate

  case class FormTodo(
		title:      String,
		content:    String,
		categoryId: Long,
		deadline:   LocalDate

	)

  case class JsValueTodo(
    id:           Long,
    title:        String,
    content:      String,
		categoryId:   Long,
    categoryName: String,
		deadline:     LocalDate
  )



  object  JsValueTodo {

	implicit val reads: Reads[JsValueTodo]   = Json.reads[JsValueTodo]
  implicit val writes: Writes[JsValueTodo] = Json.writes[JsValueTodo]

	val form: Form[FormTodo] = Form(
		mapping(
      "title"       -> nonEmptyText,
      "content"     -> nonEmptyText,
      "categoryId"  -> longNumber,
			// "state"       -> shortNumber(min = 0, max = 255),
      "deadline"    -> localDate
    )(FormTodo.apply)(FormTodo.unapply)
	)

  def write(todo: Todo.EmbeddedId, categories: Seq[Category.EmbeddedId]): JsValueTodo = {
    JsValueTodo(
      id           = todo.id,
      title        = todo.v.title,
      content      = todo.v.content,
			categoryId   = todo.v.categoryId,
      categoryName = categories.find(_.id == todo.v.categoryId).map(_.v.name).getOrElse("カテゴリー未設定"),
			deadline     = todo.v.deadline
    )
  }



}
