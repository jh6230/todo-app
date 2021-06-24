import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Todo } from '../todo';
import { TodoService } from '../todo.service';
import { CategoryService } from '../category.service';


@Component({
  selector: 'app-todo-detail',
  templateUrl: './todo-detail.component.html',
  styleUrls: ['./todo-detail.component.scss']
})
export class TodoDetailComponent implements OnInit {

  constructor(
		private todoService:     TodoService,
		private categoryService: CategoryService,
		private activeRouter:    ActivatedRoute,
		private location:        Location
		) { }

	// todoの初期化
	todos: Todo[] = []
	todo: any = []

	categories: any = []

  ngOnInit(): void {
		this.getTodo()
		this.getCategoryList()
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

	getCategoryList(): void {
		this.categoryService.getCategoryList()
			.subscribe(categories => this.categories = categories)
	}



}
