import { Component, OnInit } from '@angular/core';

import { Todo } from '../todo';
import { TodoService } from '../todo.service'

@Component({
  selector: 'app-todo-list',
  templateUrl: './todo-list.component.html',
  styleUrls: ['./todo-list.component.scss']
})
export class TodoListComponent implements OnInit {

  constructor(private todoService: TodoService ) { }

	// todoの一覧
	todos: Todo[] = []

  ngOnInit(): void {
		this.getList()
  }


	getList(): void {
		this.todoService.getTodoList()
			.subscribe(todos  => this.todos = todos)
		}

	delete(id: number): void {
		this.todos = this.todos.filter(t => t.id !== id)
		this.todoService.deleteTodo(id).subscribe()
	}
}
