package lib.persistence

import lib.model.{User, AuthToken}

import scala.concurrent.Future
import ixias.persistence.SlickRepository
import ixias.play.api.auth.token.Token.AuthenticityToken
import slick.jdbc.JdbcProfile

case class AuthTokenRepository[P <: JdbcProfile]()(implicit val driver: P)
    extends SlickRepository[AuthToken.Id, AuthToken, P]
    with db.SlickResourceProvider[P] {

  import api._

  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(AuthTokenTable, "slave") { slick =>
      slick.filter(_.id === id).result.headOption
    }

  def getByUserId(userId: User.Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(AuthTokenTable, "slave") { slick =>
      slick.filter(_.userId === userId).result.headOption
    }

  def getByToken(token: AuthenticityToken): Future[Option[EntityEmbeddedId]] =
    RunDBAction(AuthTokenTable, "slave") { slick =>
      slick.filter(_.token === token).result.headOption
    }

  def add(entity: EntityWithNoId): Future[Id] =
    RunDBAction(AuthTokenTable) { slick =>
      slick returning slick.map(_.id) += entity.v
    }

  def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] =
    RunDBAction(AuthTokenTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.update(entity.v)
        }
      } yield old
    }

  def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(AuthTokenTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.delete
        }
      } yield old
    }

}
