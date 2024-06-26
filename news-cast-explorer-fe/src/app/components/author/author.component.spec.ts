import {Message} from '../../model/message';
import {AuthorComponent} from './author.component';
import {Author} from '../../model/author';
import {} from 'jasmine';
import {MatTableDataSource} from "@angular/material/table";

describe('Author Component', () => {
  let authorComponent: AuthorComponent;

  beforeEach(() => {
    authorComponent = new AuthorComponent()
  });

  it('should be green when the selected messages are from this author', () => {
    authorComponent.messagesSelected = new MatTableDataSource<Message>([]);
    authorComponent.selectedId  = '234324';
    const author: Author = {
      name: 'WOW',
      userName: 'wow',
      id: '234324',
      createdAt: 23432423423,
      messages: authorComponent.messagesSelected.data
    };
    expect(authorComponent.calculateBackgroundAuthors(author)).toBe('green');
  });
  it('should be white when the selected messages are not from this author', () => {
    authorComponent.messagesSelected = new MatTableDataSource<Message>([]);
    const author: Author = {
      name: 'WOW',
      userName: 'wowy',
      id: '234324',
      createdAt: 23432423423,
      messages: []
    };
    expect(authorComponent.calculateBackgroundAuthors(author)).toBe('white');
  });

});
