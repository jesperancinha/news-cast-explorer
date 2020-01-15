import {Component, OnInit} from '@angular/core';
import {PageService} from "./service/page.service";
import {Page} from "./model/page";
import {animate, state, style, transition, trigger} from "@angular/animations";
import {Message} from "./model/message";
import {Author} from "./model/author";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class AppComponent implements OnInit {
  title = 'java-exercise-fe';
  dataSource: MatTableDataSource<Page>;
  messagesSelected: MatTableDataSource<Message>;
  authorsSelected: MatTableDataSource<Author>;
  filterAuthor: string = '';
  filterMessages: string = '';

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.getPages();
  }

  getPages() {
    return this.pageService.getPages()
      .subscribe(data => {
        this.dataSource = new MatTableDataSource(data);
        console.log(data[0]);
      });
  }

  pageClicked(authors: MatTableDataSource<Author>) {
    this.authorsSelected = authors
    this.messagesSelected = null;
    this.filterAuthor = '';
  }

  authorClicked(messages: MatTableDataSource<Message>) {
    this.messagesSelected = messages;
    this.filterMessages = '';
  }

}
