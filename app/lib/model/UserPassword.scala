package lib.model

import ixias.model._
import ixias.security.PBKDF2
import java.time.LocalDateTime

import UserPassword._
import ixias.model.Entity
import ixias.model.EntityModel

case class UserPassword(
    id: Option[User.Id],
    hash: String,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
) extends EntityModel[User.Id]

object UserPassword {
  def apply(id: User.Id, password: String): UserPassword#WithNoId = {
    new UserPassword(
      id = Some(id),
      hash = hash(password)
    ).toWithNoId
  }

  //パスワードをチェックする
  def hash(password: String): String = PBKDF2.hash(password)

  def verify(input: String, hash: String): Boolean = PBKDF2.compare(input, hash)

  def verifyOption(input: String, hash: String): Option[Unit] =
    if (verify(input, hash)) {
      Some(())
    } else {
      None
    }
}
