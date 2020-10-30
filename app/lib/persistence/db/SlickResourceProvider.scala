/**
  * This is a sample of Todo Application.
  * 
  */

package lib.persistence.db

import slick.jdbc.JdbcProfile

// Tableを扱うResourceのProvider
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
trait SlickResourceProvider[P <: JdbcProfile] {

  // --[ テーブル定義 ] --------------------------------------
  implicit val driver: P
  object UserTable extends UserTable 
  object TodoTable extends TodoTable

  lazy val AllTables = Seq(
    UserTable, TodoTable
    )
}
