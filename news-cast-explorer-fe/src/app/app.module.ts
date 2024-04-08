import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppComponent} from './app.component';
import {HttpClientModule} from '@angular/common/http';
import {MatTableModule} from '@angular/material/table';
import {MatTreeModule} from '@angular/material/tree';
import {MatIconModule} from '@angular/material/icon';
import {ScrollingModule} from '@angular/cdk/scrolling';
import {MatInputModule} from '@angular/material/input';
import {PageComponent} from './components/page/page.component';
import {AuthorComponent} from './components/author/author.component';
import {MessageComponent} from './components/message/message.component';
import {MatFormFieldModule} from '@angular/material/form-field';
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {RouterModule} from "@angular/router";

@NgModule({
  declarations: [
    AppComponent,
    PageComponent,
    AuthorComponent,
    MessageComponent,
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatTableModule,
    MatTreeModule,
    MatIconModule,
    ScrollingModule,
    MatInputModule,
    MatFormFieldModule,
    MatInputModule,
    RouterModule.forRoot([
      {path: '', component: PageComponent, pathMatch: 'prefix'}
    ], {useHash: false})
  ],
  providers: [],
  bootstrap: [
    AppComponent,
    PageComponent,
    AuthorComponent,
    MessageComponent,
  ],
  exports: [RouterModule],

})
export class AppModule {
}
