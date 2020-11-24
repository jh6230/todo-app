package controllers.todo

import javax.inject._
import ixias.model._
import play.api.Configuration
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import lib.model.Todo
import lib.model.Category
import lib.persistence.default.TodoRepository
import lib.persistence.default.CategoryRepository
import model.ViewValueTodo
import model.ViewValueTodoForm
import play.api.i18n.I18nSupport
import lib.model.Todo.TodoStatus
import java.lang.ProcessBuilder.Redirect
import lib.persistence.db.TodoTable
import akka.http.scaladsl.model.headers.LinkParams.title
import views.html.defaultpages.todo

case class TodoForm(
    title: String,
    categoryId: Long,
    content: String,
    state: Short
)

@Singleton
class TodoController @Inject()(
    val controllerComponents: ControllerComponents
) extends BaseController with I18nSupport {

  val Home = Redirect(routes.TodoController.list())
 

  //新規追加機能用のフォームオブジェクト
  val todoForm: Form[TodoForm] = Form(
    mapping(
      "title" -> nonEmptyText,
      "categoryId" -> longNumber,
      "content" -> nonEmptyText,
      "state" -> shortNumber(min = 0, max = 255)
    )(TodoForm.apply)(TodoForm.unapply)
  )

  //Todo一覧表示
  def list() = Action async { implicit request: Request[AnyContent] =>
    for {
      todosEmbed <- TodoRepository.all()
      categoriesEmbed <- CategoryRepository.all()
    } yield {
      val vv:ViewValueTodo = ViewValueTodo(
        head     = "Todo一覧",
        cssSrc   = Seq("main.css"),
        jsSrc    = Seq("main.js"),
        todo     = todosEmbed.map(todos =>
          (todos.v, 
            Map(todos.v.categoryId -> categoriesEmbed.find(_.id == todos.v.categoryId).get.v)))
      )
      Ok(views.html.todo.list(vv))
    }
  }
  //あいまい検索
  def search(keyword: String) = Action async {
    implicit request: Request[AnyContent] =>
      for {
        todosEmbed <- TodoRepository.search(keyword)
        categoriesEmbed <- CategoryRepository.all()
      } yield {
        val vv = ViewValueTodo(
          head     = "検索結果",
          cssSrc   = Seq("main.css"),
          jsSrc    = Seq("main.js"),
          todo     =  todosEmbed.map(todos =>
              (todos.v, 
                Map(todos.v.categoryId -> categoriesEmbed.find(_.id == todos.v.categoryId).get.v)))
        )
        Ok(views.html.todo.list(vv))
      }

  }

  //stateごとの一覧表示
  def listOfState(state: Int) = Action async {
    implicit request: Request[AnyContent] =>
      val stateCode: Short = state.toShort
      val todoStatus = TodoStatus.apply(stateCode)
      for {
        todosEmbed <- TodoRepository.todoAllByState(todoStatus)
        categoriesEmbed <- CategoryRepository.all()
      } yield {
        val vv = ViewValueTodo(
          head     = "進捗ごとのTodo一覧",
          cssSrc   = Seq("main.css"),
          jsSrc    = Seq("main.js"), 
          todo     = todosEmbed.map(todos =>
              (todos.v, 
              Map(todos.v.categoryId -> categoriesEmbed.find(_.id == todos.v.categoryId).get.v)))
        )
        Ok(views.html.todo.list(vv))
      }
  }
  //カテゴリーごとのTodo
  def todoCategory(id: Long) = Action async {
    implicit request: Request[AnyContent] =>
      val categoryId = Category.Id(id)
      for {
        todosEmbed <- TodoRepository.todoAllByCategory(categoryId)
        categoriesEmbed <- CategoryRepository.all()
      } yield {
        val vv = ViewValueTodo(
          head     = "カテゴリーごとのTodo",
          cssSrc   = Seq("main.css"),
          jsSrc    = Seq("main.js"),
          todo     = todosEmbed.map(todos =>
              (todos.v,
                Map(todos.v.categoryId -> categoriesEmbed.find(_.id == todos.v.categoryId).get.v)))
        )
        Ok(views.html.todo.list(vv))
      }
  }

  //登録画面の表示用
  def register() = Action async { implicit request: Request[AnyContent] =>
    for {
      categoriesEmbed <- CategoryRepository.all()
    } yield {
      val categories = categoriesEmbed.map(category =>
         (category.id.toString -> category.v.name)
      )
      val vv = ViewValueTodoForm(
        head     = "Todo追加",
        cssSrc   = Seq("main.css"),
        jsSrc    = Seq("main.js"),
        todoForm = todoForm
      )
      Ok(views.html.todo.add(vv, categories))
    }
  }

  //登録処理
  def add() = Action async { implicit request: Request[AnyContent] =>
    todoForm
      .bindFromRequest()
      .fold(
        (errorForm: Form[TodoForm]) => {
          for {
            categoriesEmbed <- CategoryRepository.all()
          } yield {
            val categories = categoriesEmbed.map(category => 
                (category.id.toString -> category.v.name)
            )
            val vv = ViewValueTodoForm(
              head     = "進捗ごとのTodo一覧",
              cssSrc   = Seq("main.css"),
              jsSrc    = Seq("main.js"),
              todoForm = errorForm
            )
            BadRequest(views.html.todo.add(vv, categories))
          }
        },
        (todoForm: TodoForm) => {
          val todoWithNoId = new Todo(
            id = None,
            categoryId = Category.Id(todoForm.categoryId),
            title = todoForm.title,
            content = todoForm.content,
            state = TodoStatus.apply(todoForm.state)
          ).toWithNoId
          for {
            _ <- TodoRepository.add(todoWithNoId)
          } yield {
            Redirect(routes.TodoController.list())
          }
        }
      )
  }

  //編集画面表示用
  def edit(id: Long) = Action async { implicit request: Request[AnyContent] =>
    val todoId = Todo.Id(id)
    for {
      todoEmbed <- TodoRepository.get(todoId)
      categoriesEmbed <- CategoryRepository.all()
    } yield {
      val categories = categoriesEmbed.map(category =>
          (category.id.toString -> category.v.name)
          )
      todoEmbed match {
        case Some(todoEmbed) =>
          val vv = ViewValueTodoForm(
            head     = "Todo編集",
            cssSrc   = Seq("main.css"),
            jsSrc    = Seq("main.js"),
            todoForm = todoForm.fill(
              TodoForm(
                todoEmbed.v.title,
                todoEmbed.v.categoryId,
                todoEmbed.v.content,
                todoEmbed.v.state.code
              )
            )
          )
          Ok(views.html.todo.edit(id, vv, categories))
        //idが見つからない場合はトップページにリダイレクト
        case None =>
          Redirect(routes.TodoController.list())
      }
    }
  }

  //更新処理
  def update(id: Long) = Action async { implicit request: Request[AnyContent] =>
    todoForm
      .bindFromRequest()
      .fold(
        //処理が失敗した場合
        (errorForm: Form[TodoForm]) => {
          for {
            categoriesEmbed <- CategoryRepository.all()
          } yield {
            val categories = categoriesEmbed.map(category =>
                (category.id.toString -> category.v.name)
            )
            val vv = ViewValueTodoForm(
              head     = "進捗ごとのTodo一覧",
              cssSrc   = Seq("main.css"),
              jsSrc    = Seq("main.js"),
              todoForm = errorForm
            )
            BadRequest(views.html.todo.add(vv, categories))
          }
        },
        //処理が成功した場合
        (todoForm: TodoForm) => {
          //取得したidを基にTodo[EntityEmbeddedId]をインスタンスを生成
          val todoEmbededId = new Todo(
            id = Some(Todo.Id(id)),
            categoryId = Category.Id(todoForm.categoryId),
            title = todoForm.title,
            content = todoForm.content,
            state = TodoStatus.apply(todoForm.state)
          ).toEmbeddedId //EmbededId型に変換
          for {
            todoUpdate <- TodoRepository.update(todoEmbededId)
          } yield {
            todoUpdate match {
              case None =>
                Redirect(routes.TodoController.edit(id)) //更新が失敗した場合元のページにリダイレクト
              case Some(_) =>
                Redirect(routes.TodoController.list()) //更新できたらトップページにリダイレクト
            }
          }
        }
      )
  }

  //削除処理
  def delete(id: Long) = Action async { implicit request: Request[AnyContent] =>
    val todoId = Todo.Id(id)
    for {
      todoDelete <- TodoRepository.remove(todoId)
    } yield {
      todoDelete match {
        case _ => Redirect(routes.TodoController.list())
      }
    }
  }
  
}
