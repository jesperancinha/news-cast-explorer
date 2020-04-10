import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {MatTableDataSource} from "@angular/material/table";
import {Message} from "../../model/message";
import {Author} from "../../model/author";

@Component({
    selector: 'author-component',
    templateUrl: './author.component.html',
    styleUrls: ['./author.component.css'],
    animations: [
        trigger('detailExpand', [
            state('collapsed', style({height: '0px', minHeight: '0'})),
            state('expanded', style({height: '*'})),
            transition('expanded <=> collapsed', animate('225ms cubic-bezier(0.4, 0.0, 0.2, 1)')),
        ]),
    ],
})
export class AuthorComponent implements OnInit {
    displayedAuthorsColumns: string[] = ['createdAt', 'name', 'screenName'];
    filterAuthor: string = '';
    filterMessages: string = '';
    @Input() messagesSelected: MatTableDataSource<Message>;
    @Input() authorsSelected: MatTableDataSource<Author>;
    @Output() messagesToEmit = new EventEmitter<MatTableDataSource<Message>>();

    ngOnInit() {
    }

    authorClicked(element: Author) {
        this.messagesToEmit.emit(new MatTableDataSource(element.message_dtos));
        this.filterMessages = '';
    }

    toDate(created_at: number) {
        return new Date(created_at);
    }


    applyFilterToAuthors(filterValue: any) {
        this.authorsSelected.filter = filterValue.trim().toLowerCase();
    }

    calculateBackgroundAuthors(element: Author) {
        if (this.messagesSelected && element.message_dtos === this.messagesSelected.data) {
            return 'green'
        }
        return 'white';
    }

    validateCurrentAuthorSelection() {
        return this.authorsSelected && this.authorsSelected.data && this.authorsSelected.data.length > 0;
    }
}
