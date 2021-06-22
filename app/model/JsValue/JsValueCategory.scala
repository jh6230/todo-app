package model.JsValue

import play.api.libs.json._
import lib.model.Category

case class JsValueCategory(
		id:   Long,
		name: String
	)

object JsValueCategory {

	implicit val writes: Writes[JsValueCategory] = Json.writes[JsValueCategory]

	def write(category: Category.EmbeddedId): JsValueCategory = {
		JsValueCategory(
			id   = category.id,
			name = category.v.name
		)
	}
}
