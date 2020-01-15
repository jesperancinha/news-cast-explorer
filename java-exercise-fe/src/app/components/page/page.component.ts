import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {MatTableDataSource} from "@angular/material/table";
import {Page} from "../../model/page";
import {Message} from "../../model/message";
import {Author} from "../../model/author";
import {PageService} from "../../service/page.service";

@Component({
  selector: 'page-component',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class PageComponent implements OnInit {
  dataSource: MatTableDataSource<Page>;
  displayedColumns: string[] = ['createdAt', 'duration', 'messasgesperscond', 'numberofmessages'];
  messagesSelected: MatTableDataSource<Message>;
  authorsSelected: MatTableDataSource<Author>;
  filterPage: string = '';
  filterAuthor: string = '';
  @Output() authorsToEmit = new EventEmitter<MatTableDataSource<Author>>();


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
    this.authorsToEmit.emit(this.authorsSelected);
  }


  toDate(created_at: number) {
    return new Date(created_at);
  }

  applyFilter(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  calculateBackgroundPage(element: Page) {
    if (this.authorsSelected && element.authors === this.authorsSelected.data) {
      return 'green'
    }
    return 'white';
  }

  calculateAverage(element: Page) {
    let duration: number = element.duration;
    let authors = element.authors;
    if (authors && authors.length > 0) {
      let nMessages = PageComponent.calculateNumberOfMessages(authors);
      return (nMessages / duration).toFixed(2);
    }
    return "N/A";
  }

  calculateNumberOfMessages(authors: Author[]) {
    return PageComponent.calculateNumberOfMessages(authors);
  }

  public static calculateNumberOfMessages(authors: Author[]) {
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
}
