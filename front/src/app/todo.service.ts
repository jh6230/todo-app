import { Injectable } from '@angular/core'
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { catchError, map, tap  } from 'rxjs/operators'
import { Observable } from 'rxjs/index'


@Injectable()
export class TodoService  {

  httpOptions = {
    headers: new HttpHeaders().set('Content-Type','application/json'),
  };

  constructor(private http: HttpClient) { }

	// Todo一覧取得
	getTodoList(): Observable<any> {
		return this.http.get('/api/todo/list', this.httpOptions ).pipe(
			map(response => response)
		)
	}

	// idからTodoを取得
	getTodoDetail(id: number): Observable<any> {
		return this.http.get(`api/todo/detail/${id}`, this.httpOptions).pipe(
			map(response => response)
		)
	}

	addTodo(todo: any): Observable<any> {
		return this.http.post('api/todo/add', todo, this.httpOptions).pipe(
			map(response => response)
		)
	}

	updateTodo(todo: any): Observable<any> {
		return this.http.post(`api/todo/edit/${todo.id}`, todo, this.httpOptions).pipe(
			map(response => response)
		)
	}

	deleteTodo(id: number): Observable<any> {
		return this.http.delete(`api/todo/delete/${id}`, this.httpOptions).pipe(
			map(response => response)
	)




	}


}
