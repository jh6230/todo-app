import { Injectable } from '@angular/core'
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { catchError, map, tap  } from 'rxjs/operators'
import { Observable } from 'rxjs/index'


@Injectable()
export class CategoryService  {

  httpOptions = {
    headers: new HttpHeaders().set('Content-Type','application/json'),
  };

	constructor(private http: HttpClient) { }

	getCategoryList(): Observable<any> {
		return this.http.get('api/category/list', this.httpOptions).pipe(
			map(response => response)
		)
	}


}
