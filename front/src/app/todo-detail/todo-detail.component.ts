import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Todo } from '../todo';
import { TodoService } from '../todo.service';


@Component({
  selector: 'app-todo-detail',
  templateUrl: './todo-detail.component.html',
  styleUrls: ['./todo-detail.component.scss']
})
export class TodoDetailComponent implements OnInit {

  constructor(
		private todoService:  TodoService,
		private activeRouter: ActivatedRoute,
		private location:     Location
		) { }

	// todoの初期化
	todos: Todo[] = []
	todo: any

  ngOnInit(): void {
		this.getTodo()
  }

	getTodo(): void {
		const id = Number(this.activeRouter.snapshot.paramMap.get('id'))
		this.todoService.getTodoDetail(id)
			.subscribe(todo => this.todo = todo)
	}

	getList(): void {
		this.todoService.getTodoList()
			.subscribe(todos  => this.todos = todos)
	}

	update(todo: any): void {
		this.todoService.updateTodo(todo)
			.subscribe()
			this.getList()
			this.location.back()
	}

	delete(id: number): void {
		this.todoService.deleteTodo(id).subscribe()
		this.getList()
		this.location.back()
	}



}
