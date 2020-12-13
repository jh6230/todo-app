package lib.persistence.db

import slick.jdbc.JdbcProfile

// Tableを扱うResourceのProvider
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
trait SlickResourceProvider[P <: JdbcProfile] {

  // --[ テーブル定義 ] --------------------------------------
  implicit val driver: P
  object TodoTable extends TodoTable
  object CategoryTable extends CategoryTable
  object UserTable extends UserTable
  object UserPasswordTable extends UserPasswordTable
  object AuthTokenTable extends AuthTokenTable

  lazy val AllTables = Seq(
    UserTable,
    UserPasswordTable,
    TodoTable,
    CategoryTable,
    AuthTokenTable
  )
}
