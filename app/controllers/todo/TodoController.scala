package controllers.todo

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import play.api.data.Form
import play.api.data.Form._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import lib.model.Todo
import lib.persistence.default.TodoRepository
import model.ViewValueTodo
//import play.api.i18n.I18nSupport

@Singleton
class TodoController @Inject()(
  val controllerComponents: ControllerComponents
)extends BaseController{

  def list() = Action async {implicit request: Request[AnyContent] =>
    for {
      todos <- TodoRepository.all()
    } yield {
      val vv = ViewValueTodo(
        "一覧表示",
        Seq("main.css"),
        Seq("main.js"),
        todos.map(_.v.id.get),
        todos.map(_.v.categoryId),
        todos.map(_.v.title),
        todos.map(_.v.content)
        ) 
      Ok(views.html.todo.list(vv))
    }
  }

}

//  def show() = Action async { implicit request: Request[AnyContent] =>
//    val id = Todo.Id
//    TodoRepository.get(id)
//



















  
