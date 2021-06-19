import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';

import { TodoService } from '../todo.service';

@Component({
  selector: 'app-todo-add',
  templateUrl: './todo-add.component.html',
  styleUrls: ['./todo-add.component.scss']
})
export class TodoAddComponent implements OnInit {

  constructor(
		private todoService: TodoService,
		private location:    Location
		) { }

	todo =  {
		id: null,
		title: '',
		content: ''
	}

  ngOnInit(): void {
  }

	addTodo(todo: any): void {
		this.todoService.addTodo(todo).subscribe()
		this.location.back()
	}


}
