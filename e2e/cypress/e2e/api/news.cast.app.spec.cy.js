describe('Opens application page', () => {
    const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';
    const port = Cypress.env('port') ? Cypress.env('port') : '8083';

    it('shows swagger', () => {
        cy.visit(`http://${host}:${port}`);
    });

})