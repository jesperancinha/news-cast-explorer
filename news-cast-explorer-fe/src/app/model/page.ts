import {Author} from './author';

export interface Page  {
  id: number;
  createdAt: number;
  duration: number;
  authors: Author[];
}
