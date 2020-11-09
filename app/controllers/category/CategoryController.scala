package controllers.category

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import lib.model.Category
import model.ViewValueCategory
import lib.persistence.default.CategoryRepository
import play.api.i18n.I18nSupport
import lib.model.Category.CategoryColor


@Singleton
class CategoryController @Inject()(
  val controllerComponents: ControllerComponents
)extends BaseController
with I18nSupport{

  
  //Category一覧
  def list() = Action async { implicit request: Request[AnyContent] => 
    for {
      categories <- CategoryRepository.all()
    } yield {
        val vv = ViewValueCategory(
          head     = "カテゴリー一覧表示",
          cssSrc   = Seq("main.css"),
          jsSrc    = Seq("main.js"),
          category = categories.map(_.v)
        )
      println(vv)
      Ok(views.html.category.list(vv)) 
    }
  }






  }
