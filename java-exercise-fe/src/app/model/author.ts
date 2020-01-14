import {Message} from "./message";

export interface Author {
  id: string;
  created_at: number;
  name: string;
  screenName: string;
  message_dtos: Message[];
}

