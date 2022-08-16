describe('Opens application page', () => {
    const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';
    const port = Cypress.env('port') ? Cypress.env('port') : '8083';

    it('shows swagger', () => {
        cy.visit(`http://${host}:${port}`);

        cy.wait(30000);

        cy.get('app-page-component > div > perfectscrollbar > table > tbody > tr').eq(0).click();
        cy.get('app-author-component > div > perfectscrollbar > table > tbody > tr').eq(0).click();
        cy.get('app-message-component > div > perfectscrollbar > table > tbody > tr').eq(0).should("exist");
    });

})