package lib.persistence

import scala.concurrent.Future
import ixias.persistence.SlickRepository
import lib.model.{Todo, Category}
import slick.jdbc.JdbcProfile
import lib.model.Todo.TodoStatus

//TodoRepositoryの定義
case class TodoRepository[P <: JdbcProfile]()(implicit val driver: P)
    extends SlickRepository[Todo.Id, Todo, P]
    with db.SlickResourceProvider[P] {

  import api._

  //CRUD処理

  //一覧表示
  def all(): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { slick =>
      slick.result
    }

  //あいまい検索タイトルと本文であいまい検索
  def search(keyword: String): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { slick =>
      slick
        .filter(
          v =>
            (v.title like s"%${keyword}%") || (v.content like s"%${keyword}%")
        )
        .result
    }

  //取得
  def get(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { slick =>
      slick
        .filter(_.id === id)
        .result
        .headOption
    }

  //新規追加
  def add(entity: EntityWithNoId): Future[Id] =
    RunDBAction(TodoTable) { slick =>
      slick returning slick.map(_.id) += entity.v
    }

  //更新
  def update(entity: EntityEmbeddedId): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === entity.id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.update(entity.v)
        }
      } yield old
    }
  //削除
  def remove(id: Id): Future[Option[EntityEmbeddedId]] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.id === id)
      for {
        old <- row.result.headOption
        _ <- old match {
          case None    => DBIO.successful(0)
          case Some(_) => row.delete
        }
      } yield old
    }

  //カテゴリーを削除すると、関連するTodoも削除するメソッド
  //def removeByCategory(categoryId: Long): Future[Int] =
  //  RunDBAction(TodoTable) { slick =>
  //    val row = slick.filter(_.categoryId === categoryId) //TodoのcategorId と引数で渡すcategoryIdが同じもの
  //    row.delete
  //  }

  //カテゴリーを削除した時何にも紐づいていない状態へ更新する
  def updateStatusToNull(categoryId: Category.Id): Future[Long] =
    RunDBAction(TodoTable) { slick =>
      val row = slick.filter(_.categoryId === categoryId)
      row.map(_.categoryId).update(Category.Id(0))
    }

  //カテゴリーごとのTodo一覧
  def filterByCategory(categoryId: Category.Id): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { slick =>
      slick.filter(_.categoryId === categoryId).result
    }
  //StatusごとのTodo一覧
  def filterByStatus(state: TodoStatus): Future[Seq[EntityEmbeddedId]] =
    RunDBAction(TodoTable, "slave") { slick =>
      slick.filter(_.state === state).result
    }

}
