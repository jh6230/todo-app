package model.JsValue

import play.api.libs.json._
import lib.model.{Todo, Category}
import play.api.data.Form
import play.api.data.Forms._

  case class FormTodo(
		title:   String,
		content: String
	)

  case class JsValueTodo(
    id:           Long,
    title:        String,
    content:      String
		// state:        String,
    // categoryName: String
  )

  object  JsValueTodo {

	implicit val reads: Reads[JsValueTodo]   = Json.reads[JsValueTodo]
  implicit val writes: Writes[JsValueTodo] = Json.writes[JsValueTodo]

	val form: Form[FormTodo] = Form(
		mapping(
      "title"       -> nonEmptyText,
      // "categoryId"  -> longNumber,
      "content"     -> nonEmptyText
			// "state"       -> shortNumber(min = 0, max = 255),
      // "deadline"    -> localDate
    )(FormTodo.apply)(FormTodo.unapply)
	)

  def write(todo: Todo.EmbeddedId, categories: Seq[Category.EmbeddedId]): JsValueTodo = {
    JsValueTodo(
      id           = todo.id,
      title        = todo.v.title,
      content      = todo.v.content,
			// state        = todo.v.state.name,
      // categoryName = categories.find(_.id == todo.v.categoryId).map(_.v.name).getOrElse("カテゴリーなし")
    )
  }
}
