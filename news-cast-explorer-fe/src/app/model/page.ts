import {Author} from "./author";

export interface Page  {
  created_at: number;
  duration: number;
  authors: Author[];
}
