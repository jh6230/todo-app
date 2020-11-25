package model

import lib.model.Category
import lib.model.Category.CategoryColor
import controllers.category.CategoryForm
import play.api.data.Form
import play.api.data.Forms._
import lib.persistence.default.CategoryRepository.EntityEmbeddedId 

case class ViewValueCategory(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    categories: Seq[EntityEmbeddedId]
) extends ViewValueCommon

case class ViewValueCategoryAdd(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    categoryForm: Form[CategoryForm]
) extends ViewValueCommon

case class ViewValueCategoryForm(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    categoryForm: Form[CategoryForm]
) extends ViewValueCommon


