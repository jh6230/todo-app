package controllers.api

import javax.inject._

import play.api.libs.json._
import scala.concurrent._
import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents
import lib.model.Category
import lib.persistence.default.CategoryRepository
import play.api.libs.json.JsValue
import model.JsValue.JsValueCategory

@Singleton
class CategoryApi @Inject()(
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {

	def list() = Action.async { implicit request =>
		for {
			categories <- CategoryRepository.all()
			jsVal      = categories.map(JsValueCategory.write(_))
		} yield Ok(Json.toJson(jsVal))
	}





}
