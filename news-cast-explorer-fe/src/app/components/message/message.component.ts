import {Component, Input, OnInit} from '@angular/core';
import {MatTableDataSource} from '@angular/material/table';
import {Message} from '../../model/message';
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
    selector: 'app-message-component',
    templateUrl: './message.component.html',
    styleUrls: ['./message.component.css'],
    animations: [
        trigger('detailExpand', [
            state('collapsed', style({height: '0px', minHeight: '0'})),
            state('expanded', style({height: '*'})),
            transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
        ]),
    ],
  standalone: false
})
export class MessageComponent implements OnInit {
    displayedMessagesColumns: string[] = ['id', 'createdAt', 'text'];
    filterMessages = '';
    @Input() messagesSelected: MatTableDataSource<Message>;

    ngOnInit() {
    }

    toDate(createdAt: number) {
        return new Date(createdAt);
    }


    applyFilterToMessages(filterValue: string) {
        this.messagesSelected.filter = filterValue.trim().toLowerCase();
    }

    validateCurrentMessageSelection() {
        return this.messagesSelected && this.messagesSelected.data && this.messagesSelected.data.length > 0
    }
}
