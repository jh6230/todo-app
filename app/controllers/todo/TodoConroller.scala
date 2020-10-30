package controllers.todo

import javax.inject.{Inject, Singleton}
import play.api.mvc.ControllerComponents
import play.api.mvc.BaseController
import play.api.mvc.Request
import play.api.mvc.AnyContent
import play.api.data.Form
import play.api.data.Form._
import play.api.i18n.I18nSupport
import scala.concurrent.ExecutionContext
import lib.model.Todo
import lib.persistence.TodoRepository
import scala.concurrent.Future


