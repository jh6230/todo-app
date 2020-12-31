package controllers.user

import lib.model.{User, UserPassword}
import lib.persistence.default.{UserPasswordRepository, UserRepository}
import mvc.auth.UserAuthProfile
import controllers.todo._
import model.{ViewValueLogin, ViewValueSignup}
import model.{LoginForm, SignupForm}

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.i18n.I18nSupport

import scala.concurrent._
import cats.data.{EitherT, OptionT}
import cats.implicits._
import ixias.play.api.auth.mvc.{
  AuthExtensionMethods,
  AuthenticatedOrNotActionBuilder
}
import ixias.play.api.mvc.BaseExtensionMethods

@Singleton
case class UserController @Inject()(
    controllerComponents: ControllerComponents,
    authProfile: UserAuthProfile
)(implicit ec: ExecutionContext)
    extends AuthExtensionMethods
    with BaseExtensionMethods
    with BaseController
    with I18nSupport {

  val signupForm = Form(SignupForm.dataMapping)
  val loginForm = Form(LoginForm.dataMapping)

  //サインアップフォーム
  def showSignupForm() = AuthenticatedOrNot(authProfile) { implicit req =>
    authProfile.loggedIn match {
      case Some(user) => Redirect(routes.TodoController.list())
      case None =>
        val vv = ViewValueSignup(
          head = "ユーザー登録",
          cssSrc = Seq("main.css"),
          jsSrc = Seq("main.js"),
          form = signupForm
        )
        Ok(views.html.auth.signup(vv))
    }
  }

  //ログインフォーム
  def showLoginForm(): Action[AnyContent] = AuthenticatedOrNot(authProfile) {
    implicit req =>
      authProfile.loggedIn match {
        case Some(user) => Redirect(routes.TodoController.list())
        case None =>
          val vv = ViewValueLogin(
            head = "ログインフォーム",
            cssSrc = Seq("main.css"),
            jsSrc = Seq("main.js"),
            form = loginForm
          )
          Ok(views.html.auth.login(vv))
      }
  }

  //ログイン

}
