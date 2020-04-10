import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import {HttpClientModule} from "@angular/common/http";
import {MatTableModule} from "@angular/material/table";
import {MatTreeModule} from "@angular/material/tree";
import {MatIconModule} from "@angular/material/icon";
import {ScrollingModule} from "@angular/cdk/scrolling";
import {PerfectScrollbarModule} from "ngx-perfect-scrollbar";
import {MatInputModule} from "@angular/material/input";
import {PageComponent} from "./components/page/page.component";
import {AuthorComponent} from "./components/author/author.component";
import {MessageComponent} from "./components/message/message.component";


@NgModule({
  declarations: [
    AppComponent,
    PageComponent,
    AuthorComponent,
    MessageComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatTableModule,
    MatTreeModule,
    MatIconModule,
    ScrollingModule,
    PerfectScrollbarModule,
    MatInputModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
