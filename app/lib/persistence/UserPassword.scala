package lib.persistence

import scala.concurrent.Future
import ixias.persistence.SlickRepository
import slick.jdbc.JdbcProfile
import lib.model.{User, UserPassword}

case class UserPasswordRepository[P <: JdbcProfile]()(implicit val driver: P)
    extends SlickRepository[User.Id, UserPassword, P]
    with db.SlickResourceProvider[P] {

  import api._

  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(UserPasswordTable, "slave") { slick =>
      slick.filter(_.id === id).result.headOption
    }

  def insert(entity: EntityEmbeddedId): Future[Id] =
    RunDBAction(UserPasswordTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None => slick += entity.v
          case Some(_) =>
            throw new IllegalArgumentException(
              "UserPasswordRePository: Duplicate entity id"
            )
        }
      } yield entity.id
    }

  def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] =
    RunDBAction(UserPasswordTable) { slick =>
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
    RunDBAction(UserPasswordTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.delete
        }
      } yield old
    }

  @deprecated("use add: EntityEmbededId => Future[Option[Id]]", "1.0.0")
  def add(entity: EntityWithNoId): Future[Id] =
    throw new UnsupportedOperationException("Don't use this method")

}
