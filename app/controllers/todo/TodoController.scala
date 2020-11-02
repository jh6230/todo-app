package controllers.todo

import javax.inject._
import play.api.mvc._
import play.api.data.Form
import play.api.data.Form._
import scala.concurrent.{ExecutionContext, Future}
import lib.model.Todo
import lib.persistence
//import play.api.i18n.I18nSupport

@Singleton
class TodoController @Inject()(
  val cc: ControllerComponents,
//  todoRepository:       onMySQL.TodoRepository  //TodoRepositoryの依存性注入
)(implicit ec: ExecutionContext)
extends BaseController{


//  def show(id: Long) = Action async { implicit request: Request[AnyContent] =>

  //}





















}


  
