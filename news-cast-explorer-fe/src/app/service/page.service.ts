import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable, of} from 'rxjs';
import {Page} from '../model/page';
import {Author} from '../model/author';
import {catchError, retry} from 'rxjs/operators';

const localUrl = '/api/newscast/fetcher/pages';
const localAuthorsUrl = '/api/newscast/fetcher/authors';

@Injectable({
  providedIn: 'root'
})
export class PageService {

  constructor(private http: HttpClient) {
  }

  getPages(): Observable<Page[]> {
    return this.http.get<Page[]>(localUrl).pipe(
      retry(3), catchError(this.handleError<Page[]>()));
  }

  getAuthor(id): Observable<Author> {
    return this.http.get<Author>(localAuthorsUrl + '/' + id).pipe(
      retry(3), catchError(this.handleError<Author>()));
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      console.log(`${operation} failed: ${error.message}`);

      return of(result as T);
    };
  }
}

