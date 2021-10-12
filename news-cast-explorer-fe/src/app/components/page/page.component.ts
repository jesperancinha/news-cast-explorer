import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from '@angular/animations';
import {MatTableDataSource} from '@angular/material/table';
import {Page} from '../../model/page';
import {Message} from '../../model/message';
import {Author} from '../../model/author';
import {PageService} from '../../service/page.service';
import {interval, Subscription} from 'rxjs';


@Component({
  selector: 'app-page-component',
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
  private updateSubscription: Subscription;
  dataSource: MatTableDataSource<Page>;
  displayedColumns: string[] = ['id', 'createdAt', 'duration', 'messasgesperscond', 'numberofmessages'];
  messagesSelected: MatTableDataSource<Message>;
  authorsSelected: MatTableDataSource<Author>;
  filterPage = '';
  filterAuthor = '';
  @Output() authorsToEmit = new EventEmitter<MatTableDataSource<Author>>();


  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.getPages();
    this.updateSubscription = interval(5000).subscribe(
      (val) => {
        this.addLastRead();
      });
  }


  addLastRead() {
    return this.pageService.getPages()
      .subscribe(data => {
        if (this.dataSource.data.length < data.length) {
          this.dataSource.data.push(data[data.length - 1]);
          this.dataSource.filter = '';
        }
      });
  }

  getPages() {
    return this.pageService.getPages()
      .subscribe(data => {
        this.dataSource = new MatTableDataSource(data);
      });
  }

  pageClicked(element: Page) {
    this.authorsSelected = new MatTableDataSource(element.authors);
    this.messagesSelected = null;
    this.filterAuthor = '';
    this.authorsToEmit.emit(this.authorsSelected);
  }


  toDate(createdAt: number) {
    return new Date(createdAt);
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
    const duration: number = element.duration;
    const authors = element.authors;
    if (authors && authors.length > 0) {
      const nMessages = this.calculateNumberOfMessages(authors);
      return (nMessages / duration).toFixed(2);
    }
    return 'N/A';
  }

  calculateNumberOfMessages(authors: Author[]) {
    if (authors && authors.length > 0) {
      return authors.map(author => {
        if (author) {
          if (author.messages) {
            return author.messages.length;
          }
        }
        return 0;
      }).reduce((a, b) => a + b);
    }
    return 0;
  }
}
