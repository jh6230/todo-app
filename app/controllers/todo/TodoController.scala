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
import akka.http.scaladsl.model.headers.LinkParams.title
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
          head = "一覧表示",
          cssSrc = Seq("main.css"),
          jsSrc  = Seq("main.js"),
          todo = todos.map(_.v)

      //  id =todos.map(_.v.id.get),
      //  categoryId = todos.map(_.v.categoryId),
      //  title = todos.map(_.v.title),
      //  content = todos.map(_.v.content),
      //  state = todos.map(_.v.state),
    )
      Ok(views.html.todo.list(vv))
    }
  }

}

//  def show() = Action async { implicit request: Request[AnyContent] =>
//    val id = Todo.Id
//    TodoRepository.get(id)
//



















  
