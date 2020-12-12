package lib.persistence.db

import java.time.LocalDateTime
import slick.jdbc.JdbcProfile
import ixias.persistence.model.Table
import ixias.play.api.auth.token.Token.AuthenticityToken
import lib.model.{AuthToken, User}
import scala.concurrent.duration.{Duration, SECONDS}

case class AuthTokenTable[P <: JdbcProfile]()(implicit val driver: P)
    extends Table[AuthToken, P] {
  import api._

  implicit val durationColumnType = MappedColumnType.base[Duration, String](
    {
      case Duration.Inf => "INF"
      case duration     => duration.toSeconds.toString
    }, {
      case "INF" => Duration.Inf
      case str   => Duration(str.toLong, SECONDS)
    }
  )

  //データソースの定義
  lazy val dsn = Map(
    "master" -> DataSourceName("ixias.db.mysql://master/to_do"),
    "slave" -> DataSourceName("ixias.db.mysql://slave/to_do")
  )

  //クエリの定義
  class Query extends BasicQuery(new Table(_)) {}
  lazy val query = new Query

  class Table(tag: Tag) extends BasicTable(tag, "auth_token") {
    import AuthToken._

    def id = column[AuthToken.Id]("id", O.UInt64, O.PrimaryKey, O.AutoInc)
    def userId = column[User.Id]("user_id", O.UInt64)
    def token = column[AuthenticityToken]("token", O.Utf8Char255)
    def expiry = column[Duration]("expiry", O.Utf8Char255)
    def updatedAt = column[LocalDateTime]("updated_at", O.TsCurrent)
    def createdAt = column[LocalDateTime]("created_at", O.Ts)

    type TableElementTuple = (
        Option[AuthToken.Id],
        User.Id,
        AuthenticityToken,
        Duration,
        LocalDateTime,
        LocalDateTime
    )

    def * = (id.?, userId, token, expiry, updatedAt, createdAt) <> (
      (t: TableElementTuple) =>
        AuthToken(
          t._1,
          t._2,
          t._3,
          t._4,
          t._5,
          t._6
        ),
      (v: TableElementType) =>
        AuthToken.unapply(v).map { t =>
          (
            t._1,
            t._2,
            t._3,
            t._4,
            LocalDateTime.now(),
            t._6
          )
        }
    )
  }
}
