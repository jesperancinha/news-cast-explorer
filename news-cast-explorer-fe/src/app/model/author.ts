import {Message} from './message';

export interface Author {
  id: string;
  createdAt: number;
  name: string;
  screenName: string;
  message_dtos: Message[];
}

