import {catchError, retry} from 'rxjs/internal/operators';
import {Injectable, NgModule} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {Page} from "../model/page";

const localUrl = '/api/';

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

  private handleError<Page>(operation = 'operation', result?: Page) {
    return (error: any): Observable<Page> => {
      console.error(error);
      console.log(`${operation} failed: ${error.message}`);

      return of(result as Page);
    };
  }
}

