package model

import play.api.data._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation._
import play.api.mvc.Call
import scala.util.matching.Regex

case class ViewValueSignup(
    head: String,
    cssSrc: Seq[String],
    jsSrc: Seq[String],
    title: String = "サインアップ",
    action: Call  = controllers.user.routes.UserController.signup(),
    submit: String = "サインアップ",
    form: Form[SignupFormData]
) extends ViewValueCommon

case class SignupFormData(name: String, email: String, password: String)

object SignupForm {
  val passwordPattern: Regex = """^[\x20-\x7e]{8,}$""".r

  val passwordConstraint: Constraint[String] =
    Constraint("constraints.passwordpatterncheck")({ password =>
      passwordPattern.findFirstMatchIn(password) match {
        case Some(_) => Valid
        case None =>
          Invalid {
            Seq(
              ValidationError(
                "無効なパスワードです。8字以上入力してください。"
              )
            )
          }
      }
    })

  val dataMapping: Mapping[SignupFormData] =
    mapping(
      "name" -> nonEmptyText,
      "email" -> email,
      "password" -> text.verifying(passwordConstraint)
    )(SignupFormData.apply)(SignupFormData.unapply)

  def createFormDataMap(data: Map[String, String]): SignupFormData =
    SignupFormData(
      data("name"),
      data("email"),
      data("password")
    )

}
