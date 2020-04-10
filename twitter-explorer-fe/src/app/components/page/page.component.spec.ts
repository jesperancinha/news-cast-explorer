import {PageComponent} from "./page.component";
import {Page} from "../../model/page";
import {MatTableDataSource} from "@angular/material/table";
import {Author} from "../../model/author";

describe('Page Component', () => {
    let pageComponent: PageComponent;

    beforeEach(() => {
        pageComponent = new PageComponent(null)

    });
    it('#number of messages of an empty page is 0', () => {

        expect(pageComponent.calculateNumberOfMessages([])).toBe(0);
    });
    it('#number of messages should be zero even if the author doesnt have any', () => {

        expect(pageComponent.calculateNumberOfMessages([{
            message_dtos: [],
            name: "",
            created_at: 9777,
            id: "sklndfnskf23i",
            screenName: "aloha"
        }])).toBe(0);
    });
    it('#number of messages should be one if one author has only one message', () => {
        expect(pageComponent.calculateNumberOfMessages([{
            message_dtos: [{
                text: "message",
                created_at: 234324,
                id: "23432432423"
            }],
            name: "",
            created_at: 9777,
            id: "sklndfnskf23i",
            screenName: "aloha"
        }])).toBe(1);
    });
    it('#number of messages should be 4 if one each author has two  messages', () => {
        expect(pageComponent.calculateNumberOfMessages(getTestAuthors())).toBe(4);
    });
    it('#average of 0 messages in 30 seconds in a page with authors should be N/A', () => {
        let page: Page = {
            created_at: 888888,
            duration: 30,
            authors: []
        };
        expect(pageComponent.calculateAverage(page))
            .toBe("N/A");
    });
    it('#average of 4 messages in 30 seconds should be 0.13', () => {
        let page: Page = {
            created_at: 888888,
            duration: 30,
            authors: getTestAuthors()
        };
        expect(pageComponent.calculateAverage(page))
            .toBe("0.13");
    });
    it('should be green when the selected authors are from this page', () => {
        pageComponent.authorsSelected = new MatTableDataSource<Author>([]);
        let page: Page = {
            duration: 30,
            created_at: 23432423423,
            authors: pageComponent.authorsSelected.data
        };
        expect(pageComponent.calculateBackgroundPage(page)).toBe("green");
    });
    it('should be white when the selected authors are not from this page', () => {
        pageComponent.authorsSelected = new MatTableDataSource<Author>([]);
        let page: Page = {
            duration: 30,
            created_at: 23432423423,
            authors: []
        };
        expect(pageComponent.calculateBackgroundPage(page)).toBe("white");
    });
})
;

function getTestAuthors() {
    return [
        {
            name: "",
            created_at: 9777,
            id: "sklndfnskf23i",
            screenName: "aloha",
            message_dtos: [{
                text: "message",
                created_at: 234324,
                id: "23432432423"
            }, {
                text: "message",
                created_at: 234324,
                id: "23432432423",
            },
            ],
        }, {
            name: "",
            created_at: 9777,
            id: "sklndfnskf23i",
            screenName: "aloha",
            message_dtos: [{
                text: "message",
                created_at: 234324,
                id: "23432432423"
            }, {
                text: "message",
                created_at: 234324,
                id: "23432432423",
            }]
        }];
}
