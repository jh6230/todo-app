package controllers.todo

import javax.inject._
import play.api.Configuration
import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import lib.model.Todo
import lib.persistence.default.TodoRepository
import lib.persistence.default.CategoryRepository
import model.ViewValueTodo
import model.ViewValueTodoForm
import play.api.i18n.I18nSupport
import lib.model.Todo.TodoStatus
import java.lang.ProcessBuilder.Redirect
import views.html.defaultpages.todo
import com.amazonaws.services.qldbsession.model.BadRequestException
import cats.arrow.Category

case class TodoForm(
  title:      String,
  categoryId: Long, 
  content:    String,
  state:      Short 
)

@Singleton
class TodoController @Inject()(
  val controllerComponents: ControllerComponents
)extends BaseController
with I18nSupport{

  //新規追加機能用のフォームオブジェクト
  val todoForm: Form[TodoForm]= Form(
    mapping(
      "title"      -> nonEmptyText, 
      "categoryId" -> longNumber,
      "content"    -> nonEmptyText,
      "state"      -> shortNumber(min = 0, max = 255)  
    )(TodoForm.apply)(TodoForm.unapply)
  ) 


  //Todo一覧表示
  def list() = Action async {implicit request: Request[AnyContent] =>
    for {
      todosEmbed      <- TodoRepository.all()
      categoriesEmbed <- CategoryRepository.all()
    } yield {
        val vv = ViewValueTodo( 
          head     = "Todo一覧",
          cssSrc   = Seq("main.css"),
          jsSrc    = Seq("main.js"),
          todo     = todosEmbed.map(_.v), //Seq[Todo] 
          category = categoriesEmbed.map(_.v)  //Seq[Category]
      )
      Ok(views.html.todo.list(vv))
    }
  }

  
  //登録画面の表示用
  def registar() = Action async {implicit request: Request[AnyContent] =>
    for {
      categoriesEmbed <- CategoryRepository.all()
    } yield {
        val categories = categoriesEmbed.map(_.v)
        val vv = ViewValueTodoForm(
          head       = "Todo追加",
          cssSrc     = Seq("main.css"),
          jsSrc      = Seq("main.js"),
          todoForm   = todoForm 
        )
      Ok(views.html.todo.add(vv, categories))
    }
  }


  //登録処理
  def add() = Action async {implicit request: Request[AnyContent] =>
    todoForm.bindFromRequest().fold(
      (errorForm: Form[TodoForm]) => {
        for {
          categoriesEmbed <- CategoryRepository.all()
        } yield {
            val categories = categoriesEmbed.map(_.v)
            val vv = ViewValueTodoForm(
              head     = "新規登録",
              cssSrc   = Seq("main.css"),
              jsSrc    = Seq("main.js"),
              todoForm = errorForm
            ) 
        BadRequest(views.html.todo.add(vv, categories))
        }  
      },
      (todoForm: TodoForm) =>{ 
        val  todoWithNoId = new Todo(
          id = None,
          categoryId = todoForm.categoryId,
          title      = todoForm.title,
          content    = todoForm.content,
          state      = TodoStatus.apply(todoForm.state)
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
  def edit(id: Long) = Action async {implicit request: Request[AnyContent] => 
    val todoId = Todo.Id(id)
      for {
        todoEmbed       <- TodoRepository.get(todoId)
        categoriesEmbed <- CategoryRepository.all()
      } yield  {
        val categories = categoriesEmbed.map(_.v)
        todoEmbed match {
          case Some(todoEmbed) =>
            val vv = ViewValueTodoForm(
              head     = "編集画面",
              cssSrc   = Seq("main.css"),
              jsSrc    = Seq("main.js"),
              todoForm = todoForm.fill(
                TodoForm(
                  todoEmbed.v.title, 
                  todoEmbed.v.categoryId,
                  todoEmbed.v.content,
                  todoEmbed.v.state.code))
            ) 
            Ok(views.html.todo.edit(id, vv, categories))
            //idが見つからない場合はトップページにリダイレクト 
          case None => 
            Redirect(routes.TodoController.list())
        } 
      }
  } 
  
  //更新処理
  def update(id: Long) = Action async{implicit request: Request[AnyContent] =>
    todoForm.bindFromRequest().fold(
      //処理が失敗した場合
      (errorForm: Form[TodoForm]) => { 
        for {
          categoriesEmbed <- CategoryRepository.all()
        } yield{
          val categories = categoriesEmbed.map(_.v)
          val vv = ViewValueTodoForm(
            head     = "編集画面",
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
        val  todoEmbededId = new Todo(
            id         = Some(Todo.Id(id)),
            categoryId = todoForm.categoryId,
            title      = todoForm.title,
            content    = todoForm.content,
            state      = TodoStatus.apply(todoForm.state)
        ).toEmbeddedId //EmbededId型に変換
        for {
          todoUpdate <- TodoRepository.update(todoEmbededId)
        } yield {
          todoUpdate match{
            case None    => Redirect(routes.TodoController.edit(id))//更新が失敗した場合元のページにリダイレクト 
            case Some(_) => Redirect(routes.TodoController.list())//更新できたらトップページにリダイレクト
          }
        } 
      }
    )
  }

  def delete(id: Long) = Action async{ implicit request: Request[AnyContent] => 
    val todoId = Todo.Id(id)
      for {
        todoDelete <- TodoRepository.remove(todoId)
      } yield  {
        todoDelete match {
          case None      => Redirect(routes.TodoController.list())
          case Some(_)   => Redirect(routes.TodoController.list())
        }
      }
  }
    
}


 
