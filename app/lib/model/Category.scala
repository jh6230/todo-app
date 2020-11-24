//カテゴリーモデル定義

package lib.model

import ixias.model._
import ixias.util.EnumStatus
import java.time.LocalDateTime

import Category._

case class Category(
    id: Option[Id],
    name: String,
    slug: String,
    color: CategoryColor,
    updatedAt: LocalDateTime = NOW,
    createdAt: LocalDateTime = NOW
) extends EntityModel[Id]

object Category {

  val Id = the[Identity[Id]]
  type Id = Long @@ Category
  type withNoId = Entity.WithNoId[Id, Category]
  type EmbeddedId = Entity.EmbeddedId[Id, Category]

  //viewで使用するカテゴリーのselectを定義
  val colors = Seq(
    "1"  -> "赤",
    "2"  -> "黄",
    "3"  -> "青"
  )
  
  sealed abstract class CategoryColor(val code: Short, val color: String)
      extends EnumStatus
  object CategoryColor extends EnumStatus.Of[CategoryColor] {
    case object Red extends CategoryColor(code = 1, "赤")
    case object Yellow extends CategoryColor(code = 2, "黄")
    case object Blue extends CategoryColor(code = 3, "青")
  }

  def apply(
      name: String,
      slug: String,
      color: CategoryColor
  ): Category#WithNoId =
    new Category(
      id = None,
      name = name,
      slug = slug,
      color = color
    ).toWithNoId

}
