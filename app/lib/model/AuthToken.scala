package lib.model

import ixias.model._
import ixias.play.api.auth.token.Token.AuthenticityToken
import java.time.LocalDateTime
import scala.concurrent.duration.Duration

//AuthTokenModel
import AuthToken._
case class AuthToken(
  id: Option[Id],
  userId: User.Id,
  token: AuthenticityToken,
  expiry: Duration = Duration.Inf,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
)extends EntityModel[Id]

object AuthToken{
  val id  = the[Identity[Id]]
  type Id = Long @@ AuthToken

  def apply(userId: User.Id, token: AuthenticityToken, expiry: Duration): AuthToken#WithNoId =
    new AuthToken(
      id = None,
      userId = userId,
      token = token,
      expiry = expiry
    ).toWithNoId
}
