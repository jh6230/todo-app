import javax.inject._
import play.api.inject.Binding
import play.api._
import play.api.mvc._
import play.api.http
import play.mvc.Http
import play.api.mvc.Results._
import play.api.http.HttpErrorHandler
import scala.concurrent._
import play.api.routing.Router
import views.html.defaultpages.todo

@Singleton
class ErrorHandler extends HttpErrorHandler{

  def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {  
    Future.successful(
      Status(statusCode)("A client error occurred" + message)
      )
  }

  def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {

    Future.successful(
      InternalServerError("A server error occurred" + exception.getMessage)
//      InternalServerError(Redirect(controllers.routes.TodoController.list()))




      )
  }


}

