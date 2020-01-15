import {Component, Input, OnInit} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {MatTableDataSource} from "@angular/material/table";
import {Message} from "../../model/message";
import {Author} from "../../model/author";
import {Page} from "../../model/page";
import {PageService} from "../../service/page.service";

@Component({
  selector: 'message-component',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css'],
  animations: [
    trigger('detailExpand', [
      state('collapsed', style({height: '0px', minHeight: '0'})),
      state('expanded', style({height: '*'})),
      transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
    ]),
  ],
})
export class MessageComponent implements OnInit {
  dataSource: MatTableDataSource<Page>;
  displayedMessagesColumns: string[] = ['createdAt', 'text'];
  filterMessages: string = '';
  @Input() messagesSelected: MatTableDataSource<Message>;

  ngOnInit() {
  }

  toDate(created_at: number) {
    return new Date(created_at);
  }


  applyFilterToMessages(filterValue: any) {
    this.messagesSelected.filter = filterValue.trim().toLowerCase();
  }

  validateCurrentMessageSelection() {
    return this.messagesSelected && this.messagesSelected.data && this.messagesSelected.data.length > 0
  }
}
