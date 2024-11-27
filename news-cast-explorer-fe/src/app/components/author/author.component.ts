import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {MatTableDataSource} from "@angular/material/table";
import {Message} from "../../model/message";
import {Author} from "../../model/author";

@Component({
  selector: 'app-author-component',
  templateUrl: './author.component.html',
  styleUrls: ['./author.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
  standalone: false
})
export class AuthorComponent implements OnInit {
  displayedAuthorsColumns: string[] = ['id', 'createdAt', 'name', 'userName', 'nMessages'];
  filterAuthor = '';
  filterMessages = '';
  @Input() messagesSelected: MatTableDataSource<Message>;
  @Input() authorsSelected: MatTableDataSource<Author>;
  selectedId: string;

  ngOnInit() {
  }

  authorClicked(element: Author) {
    this.filterMessages = '';
    this.messagesSelected = new MatTableDataSource<Message>(element.messages);
    this.selectedId = element.id
  }

  toDate(createdAt: number) {
    return new Date(createdAt);
  }


  applyFilterToAuthors(filterValue: any) {
    this.authorsSelected.filter = filterValue.trim().toLowerCase();
  }

  calculateBackgroundAuthors(element: Author) {
    if (element.id === this.selectedId) {
      return 'green'
    }
    return 'white';
  }

  validateCurrentAuthorSelection() {
    return this.authorsSelected && this.authorsSelected.data.length > 0;
  }
}
