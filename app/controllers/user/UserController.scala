package controllers.user

import lib.model.{User, UserPassword}
import lib.persistence.default.{UserPasswordRepository, UserRepository}
import mvc.auth.UserAuthProfile
import controllers.todo._
import model.{ViewValueLogin, ViewValueSignup}
import model.{LoginForm, SignupForm}
import model.FormErrors._

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
      case Some(user) => Redirect(controllers.todo.routes.TodoController.list())
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
        case Some(user) =>
          Redirect(controllers.todo.routes.TodoController.list())
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

  //アカウント登録
  def signup() = Action.async { implicit request =>
    signupForm.bindFromRequest.fold(
      formWithErrors => {
        val signupError = ViewValueSignup(
          head = "アカウント登録",
          cssSrc = Seq("main.css"),
          jsSrc = Seq("main.js"),
          form = formWithErrors
        )
        Future.successful(BadRequest(views.html.auth.signup(signupError)))
      },
      userData =>
        EitherT(
          for {
            userOpt <- UserRepository.getByEmail(userData.email)
          } yield userOpt match {
            case None => Right(userData)
            case Some(email) =>
              Left(
                BadRequest(
                  views.html.auth.signup(
                    ViewValueSignup(
                      head = "サインアップ",
                      cssSrc = Seq("main.css"),
                      jsSrc = Seq("main.js"),
                      form = signupForm
                        .withError(errorEmailDuplicated)
                        .fill(userData)
                    )
                  )
                )
              )
          }
        ) semiflatMap {
          case userData =>
            for {
              uid <- UserRepository.add(userData.createUser)
              _ <- UserPasswordRepository.insert(userData.createUserPassword(uid))
              result <- authProfile.loginSucceeded(uid, { _ =>
                Redirect(controllers.todo.routes.TodoController.list())
              })
            } yield result
        }
    )
  }

  //ログイン
  def login() = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => {
        val loginError = ViewValueLogin(
          head = "ログイン",
          cssSrc = Seq("main.css"),
          jsSrc = Seq("main.js"),
          form = formWithErrors
        )
        Future.successful(BadRequest(views.html.auth.login(loginError)))
      },
      userData => {
        ((for {
          user <- OptionT(UserRepository.getByEmail(userData.email))
          pass <- OptionT(UserPasswordRepository.get(user.id))
          _ <- OptionT(
            Future.successful(
              UserPassword
                .verifyOption(userData.password, pass.v.hash) //パスワードを検証する
            )
          )
        } yield user.id).semiflatMap {
          case uid =>
            authProfile.loginSucceeded(uid, { token =>
              Redirect(controllers.todo.routes.TodoController.list())
            })
        }).toRight(
            BadRequest(
              views.html.auth.login(
                ViewValueLogin(
                  head = "ログイン",
                  cssSrc = Seq("main.css"),
                  jsSrc = Seq("main.js"),
                  form = loginForm
                    .withError(errorLoginEmail)
                    .withError(errorLoginPassword)
                    .fill(userData)
                )
              )
            )
          )
          .value
      }
    )
  }

  def logout() =
    Authenticated(authProfile).async(
      implicit request =>
        authProfile.loggedIn { user =>
          authProfile.logoutSucceeded(user.id, {
            Redirect(controllers.user.routes.UserController.signup())
          })
        }
    )

}
