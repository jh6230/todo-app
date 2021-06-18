import { Component, OnInit } from '@angular/core';

import { Todo } from '../todo';

@Component({
  selector: 'app-todo-add',
  templateUrl: './todo-add.component.html',
  styleUrls: ['./todo-add.component.scss']
})
export class TodoAddComponent implements OnInit {

  constructor() { }

	todo =  {
		id: null,
		title: '',
		content: ''
	}

  ngOnInit(): void {
  }


}
