//ドメインモデル定義

package lib.model

import ixias.model._
import ixias.util.EnumStatus
import java.time.LocalDateTime

import Todo._

//Todoを表すモデル
case class Todo(
  id:         Option[Id],
  categoryId: Long,
  title:      String,
  content:    String,
  state:      TodoStatus,
  updatedAt:  LocalDateTime = NOW,
  createdAt:  LocalDateTime = NOW
) extends EntityModel[Id]


object Todo {
  
  val Id  = the[Identity[Id]]
  type Id = Long @@ Todo
  type WithNoId = Entity.WithNoId[Id, Todo]
  type EmbeddedId = Entity.EmbeddedId[Id, Todo]

  //TodoのStatusを定義
  sealed abstract class TodoStatus(val code: Short, val name: String) extends EnumStatus
  object TodoStatus extends EnumStatus.Of[TodoStatus] {
    case object IS_NOTYET    extends TodoStatus(code = 0, name = "未着手") //未着手
    case object IS_ACTIVE    extends TodoStatus(code = 1, name = "進行中") //進行中
    case object IS_FINISHED  extends TodoStatus(code = 2, name = "完了") //完了  
  }
    

  //INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(categoryId: Long, title: String, content: String, state: TodoStatus):Todo#WithNoId = 
    new Todo(
      id = None,
      categoryId = categoryId,
      title = title,
      content = content,
      state   = state 
    ).toWithNoId
  

}
