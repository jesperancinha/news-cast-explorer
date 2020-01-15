import {PageComponent} from "./page.component";

describe('Page Component', () => {
  beforeEach(() => {
  });
  it('#number of messages should be zero even if the author doesnt have any', () => {
    expect(PageComponent.calculateNumberOfMessages([{
      message_dtos: [],
      name: "",
      created_at: 9777,
      id: "sklndfnskf23i",
      screenName: "aloha"
    }])).toBe(0);
  });
  it('#number of messages should be one if one author has only one message', () => {
    expect(PageComponent.calculateNumberOfMessages([{
      message_dtos: [{
        text: "message",
        created_at: 234324,
        id: "23432432423"
      }],
      name: "",
      created_at: 9777,
      id: "sklndfnskf23i",
      screenName: "aloha"
    }])).toBe(1);
  });
});
