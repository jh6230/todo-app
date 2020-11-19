package lib.persistence.db

import slick.jdbc.JdbcProfile

// Tableを扱うResourceのProvider
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
trait SlickResourceProvider[P <: JdbcProfile] {

  // --[ テーブル定義 ] --------------------------------------
  implicit val driver: P
  object UserTable extends UserTable
  object TodoTable extends TodoTable
  object CategoryTable extends CategoryTable

  lazy val AllTables = Seq(
    UserTable,
    TodoTable,
    CategoryTable
  )
}
