import {Message} from './message';

export interface Author {
  id: string;
  createdAt: number;
  name: string;
  userName: string;
  messages: Message[];
}

