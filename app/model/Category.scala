package model

import lib.model.Category
import lib.model.Category.CategoryColor

case class ViewValueCategory(
  head:     String,
  cssSrc:   Seq[String],
  jsSrc:    Seq[String],
  category: Seq[Category]
)extends ViewValueCommon

