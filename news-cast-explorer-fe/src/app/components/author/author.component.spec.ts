import {MatTableDataSource} from '@angular/material/table';
import {Message} from '../../model/message';
import {AuthorComponent} from './author.component';
import {Author} from '../../model/author';
import {} from 'jasmine';

describe('Author Component', () => {
  let authorComponent: AuthorComponent;

  beforeEach(() => {
    authorComponent = new AuthorComponent()
  });

  it('should be green when the selected messages are from this author', () => {
    authorComponent.messagesSelected = new MatTableDataSource<Message>([]);
    const author: Author = {
      name: 'WOW',
      screenName: 'wow',
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
      screenName: 'wowy',
      id: '234324',
      createdAt: 23432423423,
      messages: []
    };
    expect(authorComponent.calculateBackgroundAuthors(author)).toBe('white');
  });

});
