import {Author} from './author';

export interface Page  {
  createdAt: number;
  duration: number;
  authors: Author[];
}
