import {MessageComponent} from './message.component';
import {MatTableDataSource} from '@angular/material/table';
import {Message} from '../../model/message';
import {} from 'jasmine';

describe('Message Component', () => {
  let messageComponent: MessageComponent;

  beforeEach(() => {
    messageComponent = new MessageComponent()
  });

  it('#all search keys should be trimmed in the front', () => {
    messageComponent.messagesSelected = new MatTableDataSource<Message>([]);
    messageComponent.applyFilterToMessages('aaaa    ');
    expect(messageComponent.messagesSelected.filter).toBe('aaaa');
  });
  it('#all search keys should be trimmed in the back', () => {
    messageComponent.messagesSelected = new MatTableDataSource<Message>([]);
    messageComponent.applyFilterToMessages('    aaaa ');
    expect(messageComponent.messagesSelected.filter).toBe('aaaa');
  });
  it('#all search keys should be trimmed both sides', () => {
    messageComponent.messagesSelected = new MatTableDataSource<Message>([]);
    messageComponent.applyFilterToMessages('   aaaa   ');
    expect(messageComponent.messagesSelected.filter).toBe('aaaa');
  });
  it('#number of messages should be zero even if the author doesnt have any', () => {
    const actual: Date = messageComponent.toDate(1579114037056);
    expect(actual.getDate()).toBe(15);
    expect(actual.getMonth()).toBe(0);
    expect(actual.getFullYear()).toBe(2020);
  });
});
