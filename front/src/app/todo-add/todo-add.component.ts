import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { TodoService } from '../todo.service';
import { Todo } from '../todo';
import { CategoryService } from '../category.service';

@Component({
  selector: 'app-todo-add',
  templateUrl: './todo-add.component.html',
  styleUrls: ['./todo-add.component.scss']
})
export class TodoAddComponent implements OnInit {

  constructor(
		private todoService:     TodoService,
		private categoryService: CategoryService,
		private location:        Location
		) { }

	todos: Todo[] = []
	todo = {
		id: null,
		title: '',
		content: '',
		categoryId: null
	}
	categories: any = []

  ngOnInit(): void {
		this.getCategoryList()
  }

	getList(): void {
		this.todoService.getTodoList()
			.subscribe(todos  => this.todos = todos)
	}

	addTodo(todo: any): void {
		this.todoService.addTodo(todo).subscribe()
		this.getList()
		this.location.back()
	}

	getCategoryList(): void {
		this.categoryService.getCategoryList()
			.subscribe(categories => this.categories = categories)
	}


}
