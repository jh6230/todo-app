//ドメインモデル定義

package lib.model

import ixias.model._
import ixias.util.EnumStatus
import java.time.LocalDateTime
import lib.model._

import Todo._

//Todoを表すモデル
case class Todo(
    id:         Option[Id],
    categoryId: Category.Id,
    title: String,
    content: String,
    state: TodoStatus,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

object Todo {

  val Id = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId[Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]



  //TodoのStatusを定義
  sealed abstract class TodoStatus(val code: Short, val name: String)
      extends EnumStatus
  object TodoStatus extends EnumStatus.Of[TodoStatus] {
    case object Notyet extends TodoStatus(0, "未着手")
    case object Active extends TodoStatus(1, "進行中")
    case object Finished extends TodoStatus(2, "完了")
  }

  //INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(
    categoryId: Category.Id,
    title: String,
    content: String,
    state: TodoStatus,
  ): WithNoId =
    Entity.WithNoId{
      new Todo(
        None,
        categoryId,
        title,
        content,
        state
      )
    }
  

}
