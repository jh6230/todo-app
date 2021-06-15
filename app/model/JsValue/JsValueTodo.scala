package model.JsValue

import play.api.libs.json._
import lib.model.{Todo, Category}

  case class JsValueTodo(
    id:          Long,
    title:       String,
    content:     String,
    categoryName:String
  )

  object  JsValueTodo {

  implicit val writes: Writes[JsValueTodo] = Json.writes[JsValueTodo]

  def apply(todo: Todo.EmbeddedId, categories: Seq[Category.EmbeddedId]): JsValueTodo = {
    JsValueTodo(
      id           = todo.id,
      title        = todo.v.title,
      content      = todo.v.content,
      categoryName = categories.find(_.id == todo.v.categoryId).map(_.v.name).getOrElse("カテゴリーなし")
    )
  }
}
