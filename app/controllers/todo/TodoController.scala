package controllers.todo

import javax.inject._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Form._
import scala.concurrent.{ExecutionContext, Future}
import lib.model.Todo
import lib.persistence.default.TodoRepository
import ixias.persistence.SlickRepository
//import play.api.i18n.I18nSupport

@Singleton
class TodoController @Inject()(
  val cc: ControllerComponents
)(implicit ec: ExecutionContext)
extends BaseController{

  def list() = Action async {implicit request: Request[AnyContent] =>
      for {
        results <-  TodoRepository.all()
      }  yield 
      Ok(views.html.todo.list(results))

    


  }





















}


  
