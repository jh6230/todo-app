package model

import lib.model.Category
import lib.model.Category.CategoryColor
import controllers.category.CategoryForm
import play.api.data.Form
import play.api.data.Forms._

case class ViewValueCategory(
  head:     String,
  category: Seq[Category]
)extends ViewValueCommon

case class ViewValueCategoryForm(
  head:          String,
  categoryForm:  Form[CategoryForm]
)extends ViewValueCommon


