import {MatTableDataSource} from "@angular/material/table";
import {Message} from "../../model/message";
import {AuthorComponent} from "./author.component";
import {Author} from "../../model/author";

describe('Author Component', () => {
    let authorComponent: AuthorComponent;

    beforeEach(() => {
        authorComponent = new AuthorComponent()
    });

    it('should be green when the selected messages are from this author', () => {
        authorComponent.messagesSelected = new MatTableDataSource<Message>([]);
        let author: Author = {
            name: "WOW",
            screenName: "wowy",
            id: "234324",
            created_at: 23432423423,
            message_dtos: authorComponent.messagesSelected.data
        };
        expect(authorComponent.calculateBackgroundAuthors(author)).toBe("green");
    });
    it('should be white when the selected messages are not from this author', () => {
        authorComponent.messagesSelected = new MatTableDataSource<Message>([]);
        let author: Author = {
            name: "WOW",
            screenName: "wowy",
            id: "234324",
            created_at: 23432423423,
            message_dtos: []
        };
        expect(authorComponent.calculateBackgroundAuthors(author)).toBe("white");
    });

});
