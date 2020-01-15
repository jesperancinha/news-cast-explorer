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
  displayedColumns: string[] = ['createdAt', 'duration', 'messasgesperscond', 'numberofmessages'];
  displayedAuthorsColumns: string[] = ['createdAt', 'name', 'screenName'];
  displayedMessagesColumns: string[] = ['createdAt', 'text'];
  messagesSelected: MatTableDataSource<Message>;
  authorsSelected: MatTableDataSource<Author>;
  filterPage: string = '';
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

  pageClicked(element: Page) {
    this.authorsSelected = new MatTableDataSource(element.authors);
    this.messagesSelected = null;
    this.filterAuthor = '';
  }

  authorClicked(element: Author) {
    this.messagesSelected = new MatTableDataSource(element.message_dtos);
    this.filterMessages = '';
  }

  toDate(created_at: number) {
    return new Date(created_at);
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  applyFilterToAuthors(filterValue: any) {
    this.authorsSelected.filter = filterValue.trim().toLowerCase();
  }

  applyFilterToMessages(filterValue: any) {
    this.messagesSelected.filter = filterValue.trim().toLowerCase();
  }

  calculateBackgroundPage(element: Page) {
    if (this.authorsSelected && element.authors === this.authorsSelected.data) {
      return 'green'
    }
    return 'white';
  }

  calculateBackgroundAuthors(element: Author) {
    if (this.messagesSelected && element.message_dtos === this.messagesSelected.data) {
      return 'green'
    }
    return 'white';
  }

  calculateAverage(element: Page) {
    let duration: number = element.duration;
    let authors = element.authors;
    if (authors && authors.length > 0) {
      let nMessages = this.calculateNumberOfMessages(authors);
      return (nMessages / duration).toFixed(2);
    }
    return "N/A";
  }

  calculateNumberOfMessages(authors: Author[]) {
    if (authors && authors.length > 0) {
      return authors.map(author => {
        if (author) {
          if (author.message_dtos) {
            return author.message_dtos.length;
          }
        }
        return 0;
      }).reduce((a, b) => a + b);
    }
    return 0;
  }

  validateCurrentAuthorSelection() {
    return this.authorsSelected && this.authorsSelected.data && this.authorsSelected.data.length > 0;
  }

  validateCurrentMessageSelection() {
    return this.messagesSelected && this.messagesSelected.data && this.messagesSelected.data.length > 0
  }
}
