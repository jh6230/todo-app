package lib.model

import ixias.model._
import ixias.util.EnumStatus

import java.time.LocalDateTime
// ユーザーを表すモデル
import User._
case class User(
    id: Option[Id],
    name: String,
    email: String,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
) extends EntityModel[Id]
// コンパニオンオブジェクト
//~~~~~~~~~~~~~~~~~~~~~~~~
object User {
  val Id = the[Identity[Id]]
  type Id = Long @@ User
  // INSERT時のIDがAutoincrementのため,IDなしであることを示すオブジェクトに変換
  def apply(name: String, email: String): User#WithNoId =
    new User(
      id = None,
      name = name,
      email = email
    ).toWithNoId
}
