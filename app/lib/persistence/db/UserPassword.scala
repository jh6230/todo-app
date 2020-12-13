package lib.persistence.db

import java.time.LocalDateTime
import slick.jdbc.JdbcProfile
import lib.model.{User, UserPassword}
import ixias.persistence.model.Table

case class UserPasswordTable[P <: JdbcProfile]()(implicit val driver: P)
    extends Table[UserPassword, P] {
  import api._

  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do"),
    "slave" -> DataSourceName("ixias.db.mysql://slave/to_do")
  )

  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query

  class Table(tag: Tag) extends BasicTable(tag, "user_password") {
    import UserPassword._

    def id = column[User.Id]("user_id", O.UInt64, O.PrimaryKey)
    def hash = column[String]("hash", O.Utf8Char255)
    def updatedAt = column[LocalDateTime]("updated_at", O.TsCurrent)
    def createdAt = column[LocalDateTime]("created_at", O.Ts)

    type TableElementTuple = (
        Option[User.Id],
        String,
        LocalDateTime,
        LocalDateTime
    )

    def * = (id.?, hash, updatedAt, createdAt) <> (
      (t: TableElementTuple) =>
        UserPassword(
          t._1,
          t._2,
          t._3,
          t._4
        ),
      (v: TableElementType) =>
        UserPassword.unapply(v).map { t =>
          (
            t._1,
            t._2,
            LocalDateTime.now(),
            t._4
          )
        }
    )
  }
}
