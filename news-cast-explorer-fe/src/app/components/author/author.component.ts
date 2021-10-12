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
})
export class AuthorComponent implements OnInit {
    displayedAuthorsColumns: string[] = ['id', 'createdAt', 'name', 'screenName', 'nMessages'];
    filterAuthor = '';
    filterMessages = '';
    @Input() messagesSelected: MatTableDataSource<Message>;
    @Input() authorsSelected: MatTableDataSource<Author>;
    @Output() messagesToEmit = new EventEmitter<MatTableDataSource<Message>>();

    ngOnInit() {
    }

    authorClicked(element: Author) {
        this.messagesToEmit.emit(new MatTableDataSource(element.messages));
        this.filterMessages = '';
    }

    toDate(createdAt: number) {
        return new Date(createdAt);
    }


    applyFilterToAuthors(filterValue: any) {
        this.authorsSelected.filter = filterValue.trim().toLowerCase();
    }

    calculateBackgroundAuthors(element: Author) {
        if (this.messagesSelected && element.messages === this.messagesSelected.data) {
            return 'green'
        }
        return 'white';
    }

    validateCurrentAuthorSelection() {
        return this.authorsSelected && this.authorsSelected.data && this.authorsSelected.data.length > 0;
    }
}
