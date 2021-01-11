package lib.persistence.db

import java.time.LocalDateTime
import slick.jdbc.JdbcProfile
import ixias.persistence.model.Table

import lib.model.User

// UserTable: Userテーブルへのマッピングを行う
//~~~~~~~~~~~~~~
case class UserTable[P <: JdbcProfile]()(implicit val driver: P)
    extends Table[User, P] {
  import api._

  // Definition of DataSourceName
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do"),
    "slave" -> DataSourceName("ixias.db.mysql://slave/to_do")
  )

  // Definition of Query
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query

  // Definition of Table
  //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
  class Table(tag: Tag) extends BasicTable(tag, "user") {
    import User._
    def id = column[User.Id]("id", O.UInt64, O.PrimaryKey, O.AutoInc)
    def name = column[String]("name", O.Utf8Char255)
    def email = column[String]("email", O.AsciiChar64)
    def updatedAt = column[LocalDateTime]("updated_at", O.TsCurrent)
    def createdAt = column[LocalDateTime]("created_at", O.Ts)

    type TableElementTuple = (
        Option[User.Id],
        String,
        String,
        LocalDateTime,
        LocalDateTime
    )

    // DB <=> Scala の相互のmapping定義
    def * = (id.?, name, email, updatedAt, createdAt) <> (
      // Tuple(table) => Model
      (t: TableElementTuple) =>
        User(
          t._1,
          t._2,
          t._3,
          t._4,
          t._5
        ),
      // Model => Tuple(table)
      (v: TableElementType) =>
        User.unapply(v).map { t =>
          (
            t._1,
            t._2,
            t._3,
            LocalDateTime.now(),
            t._5
          )
        }
    )
  }
}
