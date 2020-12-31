import play.api.http.HttpErrorHandler
import play.api.mvc._
import play.api.mvc.Results._
import play.api.mvc.Result
import scala.concurrent._
import javax.inject.Singleton
import play.api.http.Writeable._

@Singleton
class ErrorHandler extends HttpErrorHandler {

  def onClientError(
      request: RequestHeader,
      statusCode: Int,
      message: String
  ): Future[Result] = {
    Future.successful(
      Status(statusCode)("A client error occurred: " + message)
    )
  }

  def onServerError(
      request: RequestHeader,
      exception: Throwable
  ): Future[Result] = {
    Future.successful(
      InternalServerError("A server error occurred: " + exception.getMessage)
    )
  }
}
