package model

import play.api.data.{Form, Mapping}
import play.api.data.Forms._
import play.api.mvc.Call

case class ViewValueLogin(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    title: String = "ログイン",
//    action: Call = controllers.routes.UserController.login(),
    submit: String = "ログイン",
    form: Form[LoginFormData]
) extends ViewValueCommon

case class LoginFormData(email: String, password: String)

object LoginForm {

  val dataMapping: Mapping[LoginFormData] = mapping(
    "email" -> email,
    "password" -> text(minLength = 8)
  )(LoginFormData.apply)(LoginFormData.unapply)

  val emptyDataMap = Map(
    "email" -> "",
    "password" -> ""
  )

  def createFormDataFormMap(data: Map[String, String]): LoginFormData =
    LoginFormData(
      data("email"),
      data("password")
    )

}
