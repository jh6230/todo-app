import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { TodoService } from '../todo.service';


@Component({
  selector: 'app-todo-detail',
  templateUrl: './todo-detail.component.html',
  styleUrls: ['./todo-detail.component.scss']
})
export class TodoDetailComponent implements OnInit {

  constructor(
		private todoService: TodoService,
		private router:      ActivatedRoute
		) { }

	todo: any = []

  ngOnInit(): void {
		this.getDetail()
  }

	getDetail(): void {
		const id = Number(this.router.snapshot.paramMap.get('id'))
		this.todoService.getTodoDetail(id)
			.subscribe(todo => this.todo = todo)

	}

}
