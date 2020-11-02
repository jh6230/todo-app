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
import model.ViewValueHome
//import play.api.i18n.I18nSupport

@Singleton
class TodoController @Inject()(
  val controllerComponents: ControllerComponents
)extends BaseController{

  def list() = Action async {implicit request: Request[AnyContent] =>
    for {
      todos <- TodoRepository.all()
    } yield {
      todos.v


//    コンパイルを通すために記述
//    println(todos)
//      val vv = ViewValueHome(
//      title  = "Home",
//      cssSrc = Seq("main.css"),
//      jsSrc  = Seq("main.js")
//    )
//    
//     (Ok(views.html.Home(vv)))
//    }
 

//  def show() = Action async { implicit request: Request[AnyContent] =>
//    val id = Todo.Id
//    TodoRepository.get(id)
//


}

















}


  
