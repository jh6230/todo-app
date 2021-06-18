//ドメインモデル定義

package lib.model

import ixias.model._
import ixias.util.EnumStatus
import java.time.LocalDateTime
import java.time.LocalDate

import Todo._

//Todoを表すモデル
case class Todo(
    id: Option[Id],
//    userId: User.Id,
    categoryId: Category.Id  = Category.Id(14),
    title: String,
    content: String,
    state: TodoStatus        = TodoStatus.Notyet,
    deadline: LocalDate      = LocalDate.now(),
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

object Todo {

  val Id = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId[Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  val statusDefault = Seq("0" -> "未着手") //新規登録時用、新規追加の時は未着手しか選べない
  val statuses = Seq("0" -> "未着手", "1" -> "進行中", "2" -> "完了") //編集時用

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
//      userId: User.Id,
      categoryId: Category.Id,
      title: String,
      content: String,
      state: TodoStatus,
      deadline: LocalDate
  ): WithNoId =
    Entity.WithNoId {
      new Todo(
        None,
//        userId,
        categoryId,
        title,
        content,
        state,
        deadline
      )
    }
}
