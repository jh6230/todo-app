package controllers.category

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import lib.model.{ Todo, Category }
import model.{ ViewValueCategory, ViewValueCategoryForm }
import lib.persistence.default.{ TodoRepository, CategoryRepository }
import controllers.todo
import play.api.i18n.I18nSupport
import lib.model.Category.CategoryColor

case class CategoryForm(
    name:  String,
    slug:  String,
    color: Short
)

@Singleton
class CategoryController @Inject()(
    val controllerComponents: ControllerComponents
) extends BaseController with I18nSupport{

  //新規登録用のフォームオブジェクト
  val categoryForm: Form[CategoryForm] = Form(
    mapping(
      "name" -> nonEmptyText,
      "slug" -> nonEmptyText.verifying(
        error = "半角英数字のみ入力してください",
        constraint = _.matches("""^[0-9a-zA-Z]+$""")
      ),
      "color" -> shortNumber(min = 0, max = 255)
    )(CategoryForm.apply)(CategoryForm.unapply)
  )

  //カテゴリー一覧表示
  def list() = Action async { implicit request: Request[AnyContent] =>
    for {
      categories <- CategoryRepository.all()
    } yield {
      val vv = ViewValueCategory(
        head     = "カテゴリー一覧",
        cssSrc   = Seq("main.css"),
        jsSrc    = Seq("main.js"),
        category = categories.map(_.v)
      )
      Ok(views.html.category.list(vv))
    }
  }

  //登録画面の表示用
  def register() = Action { implicit request: Request[AnyContent] =>
    val vv = ViewValueCategoryForm(
      head     = "カテゴリー一覧",
      cssSrc   = Seq("main.css"),
      jsSrc    = Seq("main.js"),
      categoryForm = categoryForm
    )
    Ok(views.html.category.add(vv))
  }

  //登録処理
  def add() = Action async { implicit request: Request[AnyContent] =>
    categoryForm
      .bindFromRequest()
      .fold(
        (categoryForm: Form[CategoryForm]) => {
          val vv = ViewValueCategoryForm(
            head     = "カテゴリー 追加",
            cssSrc   = Seq("main.css"),
            jsSrc    = Seq("main.js"),
            categoryForm = categoryForm
          )
          Future.successful(BadRequest(views.html.category.add(vv)))
        },
        (categoryForm: CategoryForm) => {
          val categoryWithNoId = new Category(
            id = None,
            name = categoryForm.name,
            slug = categoryForm.slug,
            color = CategoryColor.apply(categoryForm.color)
          ).toWithNoId
          for {
            _ <- CategoryRepository.add(categoryWithNoId)
          } yield {
            Redirect(routes.CategoryController.list())
          }
        }
      )
  }

  //編集画面表示用
  def edit(id: Long) = Action async { implicit request: Request[AnyContent] =>
    val categoryId = Category.Id(id)
    for {
      category <- CategoryRepository.get(categoryId)
    } yield {
      category match {
        case Some(category) =>
          val vv = ViewValueCategoryForm(
                head     = "カテゴリー 編集",
                cssSrc   = Seq("main.css"),
                jsSrc    = Seq("main.js"),
                categoryForm = categoryForm.fill(
              CategoryForm(
                category.v.name,
                category.v.slug,
                category.v.color.code
              )
            )
          )
          Ok(views.html.category.edit(id, vv))
        //idが見つからない場合はトップページにリダイレクト
        case None =>
          Redirect(routes.CategoryController.list())
      }
    }
  }

  //更新処理
  def update(id: Long) = Action async { implicit request: Request[AnyContent] =>
    categoryForm
      .bindFromRequest()
      .fold(
        //処理が失敗した場合
        (errorForm: Form[CategoryForm]) => {
          val vv = ViewValueCategoryForm(
            head         = "カテゴリー編集",
            cssSrc       = Seq("main.css"),
            jsSrc        = Seq("main.js"),
            categoryForm = errorForm
          )
          Future.successful(BadRequest(views.html.category.edit(id, vv)))
        },
        //処理が成功した場合
        (categoryForm: CategoryForm) => {
          val categoryEmbededId = new Category(
            id = Some(Category.Id(id)),
            name = categoryForm.name,
            slug = categoryForm.slug,
            color = CategoryColor.apply(categoryForm.color)
          ).toEmbeddedId
          for {
            categoryUpdate <- CategoryRepository.update(categoryEmbededId)
          } yield {
            categoryUpdate match {
              case None =>
                Redirect(routes.CategoryController.edit(id)) //更新が失敗したら元の画面へリダイレクト
              case Some(_) =>
                Redirect(routes.CategoryController.list()) //更新できたら一覧へリダイレクト
            }
          }
        }
      )
  }

  //削除処理
  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    val categoryId = Category.Id(id)
    for {
      categoryDelete <- CategoryRepository.remove(categoryId)
      todosDelete <- TodoRepository.insertNullByCategory(categoryId)
    } yield {

      (todosDelete, categoryDelete) match {
        case _ => Redirect(routes.CategoryController.list()) //削除へのルーティング
      }
    }
  }


}
