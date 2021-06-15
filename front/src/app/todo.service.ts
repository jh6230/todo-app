import { Injectable } from '@angular/core'
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { map  } from 'rxjs/operators'
import { Observable } from 'rxjs/index'


@Injectable()
export class TodoService  {

  httpOptions = {
    headers: new HttpHeaders().set('Content-Type','application/json'),
  };

  constructor(private http: HttpClient) { }

	// Todo一覧取得
	getTodoList() {
		return this.http.get('/api/todo/list', this.httpOptions ).pipe(
			map(response => response)
		)
	}

	// idからTodoを取得
	getTodoDetail(id: number) {
		return this.http.get(`api/todo/detail/${id}`, this.httpOptions).pipe(
			map(response => response)
		)

	}


}
