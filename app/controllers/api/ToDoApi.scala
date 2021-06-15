package controllers.api

import javax.inject._

import play.api.libs.json._
import scala.concurrent._
import play.api.mvc.BaseController
import play.api.mvc.ControllerComponents
import lib.model.{ Todo, Category }
import lib.persistence.default.{ TodoRepository, CategoryRepository }
import play.api.libs.json.JsValue
import model.JsValue.JsValueTodo

@Singleton
class TodoApi @Inject()(
  val controllerComponents: ControllerComponents
)(implicit ec: ExecutionContext) extends BaseController {

  // Todoの一覧を取得するAPI
  def list() =  Action.async { implicit request =>
    for {
      todos       <- TodoRepository.all()
      caategories <- CategoryRepository.all()
      jsVal       = todos.map(JsValueTodo(_, caategories))
    } yield Ok(Json.toJson(jsVal))
  }

	// idからTodoを取得
	def get(id: Long) = Action.async { implicit request =>
		for {
			todo       <- TodoRepository.get(Todo.Id(id))
			categories <- CategoryRepository.all()
			jsval      = todo.map(JsValueTodo(_, categories))
		} yield Ok(Json.toJson(jsval))
	}

	// Todoを削除する
	def delete(id: Long) = Action.async { implicit request =>
		for {
			_ <- TodoRepository.remove(Todo.Id(id))
		} yield NoContent
	}

}
