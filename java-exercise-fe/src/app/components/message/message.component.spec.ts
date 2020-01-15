import {MessageComponent} from "./message.component";

describe('Page Component', () => {
  let messageComponent: MessageComponent;

  beforeEach(() => {
    messageComponent = new MessageComponent()
  });

  it('#number of messages should be zero even if the author doesnt have any', () => {
    let actual:Date = messageComponent.toDate(1579114037056);
    expect(actual.getDate()).toBe(Number(15));
    expect(actual.getMonth()).toBe(0);
    expect(actual.getFullYear()).toBe(2020);
  });
});
