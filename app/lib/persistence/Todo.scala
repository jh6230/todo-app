package lib.persistence

import scala.concurrent.Future
import ixias.persistence.SlickRepository
import lib.model.Todo
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
      RunDBAction(TodoTable, "slave"){
        slick => slick.result
      }

    //あいまい検索
    def search(keyword: String): Future[Seq[EntityEmbeddedId]] = 
      RunDBAction(TodoTable, "slave"){
        slick => slick.filter(_.title like s"%${keyword}%").result
      }
    
    //取得
    def get(id: Id): Future[Option[EntityEmbeddedId]] = 
      RunDBAction(TodoTable, "slave") {slick => slick 
        .filter(_.id === id).result.headOption
      }

    //新規追加
    def add(entity: EntityWithNoId):Future[Id] = 
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
    def remove(id: Id):Future[Option[EntityEmbeddedId]] = 
      RunDBAction(TodoTable) { slick =>
        val row = slick.filter(_.id === id)
        for {
          old <- row.result.headOption
          _   <- old match {
            case None    => DBIO.successful(0)
            case Some(_) =>row.delete
          } 
        } yield old 
     }

    //カテゴリーを削除すると、関連するTodoも削除するメソッド
    def removeAllByCategory(categoryId: Long):Future[Int] = 
      RunDBAction(TodoTable) { slick =>
        val row = slick.filter(_.categoryId === categoryId) //TodoのcategorId と引数で渡すcategoryIdが同じもの
        row.delete
      }
    //カテゴリーごとのTodo一覧
    def todoAllByCategory(categoryId: Long):Future[Seq[EntityEmbeddedId]] = 
       RunDBAction(TodoTable, "slave"){
        slick => slick.filter(_.categoryId === categoryId).result
    }
    //StatusごとのTodo一覧
    def todoAllByState(state: TodoStatus):Future[Seq[EntityEmbeddedId]] = 
      RunDBAction(TodoTable, "slave"){
        slick => slick.filter(_.state === state).result
    }

     
}

  

