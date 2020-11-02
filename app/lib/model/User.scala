/**
  * This is a sample of Todo Application.
  * 
  */

package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime

// ユーザーを表すモデル
//~~~~~~~~~~~~~~~~~~~~
import User._
case class User(
  id:        Option[Id],
  name:      String,
  age:       Short,
  state:     UserStatus,
  updatedAt: LocalDateTime = NOW,
  createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object User {

  val  Id = the[Identity[Id]]
  type Id = Long @@ User
  type WithNoId = Entity.WithNoId [Id, User]
  type EmbeddedId = Entity.EmbeddedId[Id, User]

  // ステータス定義
  //~~~~~~~~~~~~~~~~~
  sealed abstract class UserStatus(val code: Short, val name: String) extends EnumStatus
  object UserStatus extends EnumStatus.Of[UserStatus] {
    case object IS_INACTIVE extends UserStatus(code = 0,   name = "無効")
    case object IS_ACTIVE   extends UserStatus(code = 100, name = "有効")
  }

  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(name: String, age: Short, state: UserStatus): WithNoId = {
    new Entity.WithNoId(
      new User(
        id    = None,
        name  = name,
        age   = age,
        state = state
      )
    )
  }
}
