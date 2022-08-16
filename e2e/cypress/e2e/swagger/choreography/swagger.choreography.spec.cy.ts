describe('Swagger Tests Saga Choreography', () => {
    const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';
    const port = Cypress.env('port') ? Cypress.env('port') : '8083';
    const baseUrl = Cypress.env('port') ? '/api/saga/choreography/swagger-ui' : '/api/saga/swagger-ui';
    const docPath = Cypress.env('port') ? '/api/saga/choreography/v3/api-docs' : '/api/saga/v3/api-docs';

    it('shows swagger', () => {
        cy.visit(`http://${host}:${port}${baseUrl}/index.html`);
        cy.get('input[class="download-url-input"]').should("exist");
        cy.get('input[class="download-url-input"]').clear().type(docPath);
        cy.get('button').contains('Explore').click();
        cy.get('h2', {timeout: 10000}).contains('OpenAPI definition', {timeout: 10000}).should('not.be.null');
        cy.wait(1000);

        cy.get('div[class="servers"] > label > select > option').should('have.value', 'http://localhost:9000/api/saga/choreography')
    });

})