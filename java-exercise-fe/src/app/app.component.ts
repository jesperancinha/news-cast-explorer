import {Component, OnInit} from '@angular/core';
import {PageService} from "./service/page.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent implements OnInit {
  title = 'java-exercise-fe';

  constructor(private pageService: PageService) {
  }

  ngOnInit() {
    this.getPages();
  }

  getPages() {
    this.pageService.getPages()
      .subscribe(data => {
        data.forEach(page => console.log(page.duration));
      });
  }

}
