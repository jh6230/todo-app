package controllers.todo

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import lib.model.Todo
import lib.persistence.default.TodoRepository
import model.ViewValueTodo
import model.ViewValueTodoForm
import play.api.i18n.I18nSupport

case class TodoForm(title: String, content: String)

@Singleton
class TodoController @Inject()(
  val controllerComponents: ControllerComponents
)extends BaseController
with I18nSupport{

  //新規追加機能用のフォームオブジェクト
  val todoForm: Form[TodoForm]= Form(
    mapping(
      "title"    -> nonEmptyText, 
      "content"  -> nonEmptyText
    )(TodoForm.apply)(TodoForm.unapply)
  ) 



  //Todo一覧表示
  def list() = Action async {implicit request: Request[AnyContent] =>
    for {
      todos <- TodoRepository.all()
    } yield {
        val vv = ViewValueTodo(
          head = "一覧表示",
          cssSrc = Seq("main.css"),
          jsSrc  = Seq("main.js"),
          todo = todos.map(_.v)
        )
      Ok(views.html.todo.list(vv))
    }
  }

  //登録画面の表示用
  def registar() = Action { implicit request: Request[AnyContent] => 
    val vv = ViewValueTodoForm(
      head     = "新規登録",
      cssSrc   = Seq("main.css"),
      jsSrc    = Seq("main.js"),
      todoForm = todoForm
      )
    Ok(views.html.todo.add(vv))
  }


  def add() = Action async {implicit request: Request[AnyContent] =>
    Future.successful(NoContent)



  }  


}


  
