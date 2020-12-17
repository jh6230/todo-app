package mvc.auth

import lib.model.User
import lib.persistence.default.UserRepository

import javax.inject._
import scala.concurrent.{Future, ExecutionContext}

import play.api.Environment
import play.api.mvc.RequestHeader

import ixias.play.api.auth.token.Token
import ixias.play.api.auth.token.TokenViaSession
import ixias.play.api.auth.container.Container

case class UserAuthProfile @Inject()(container: AuthTokenContainer)(
    implicit ec: ExecutionContext
) extends ixias.play.api.auth.mvc.AuthProfile[User.Id, User, Unit] {

  val env: Environment = Environment.simple()
  val tokenAccessor: Token = TokenViaSession("user")
  val datastore: Container[Id] = container
  val executionContext: ExecutionContext = ec

  def resolve(
      id: Id
  )(implicit request: RequestHeader): Future[Option[AuthEntity]] = {
    UserRepository.get(id)
  }

}
