describe('Opens application page', () => {
    const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';
    const port = Cypress.env('port') ? Cypress.env('port') : '4200';

    it('shows application page', () => {
        cy.visit(`http://${host}:${port}`);

        cy.wait(30000);
        cy.get('app-page-component > div > div > table > tbody > tr').eq(0).click();
        cy.get('app-author-component > div > div > table > tbody > tr').eq(0).click();
        cy.get('app-message-component > div > div > table > tbody > tr').eq(0).should("exist");
    });

})