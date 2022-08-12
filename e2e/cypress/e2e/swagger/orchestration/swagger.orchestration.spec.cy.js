describe('Swagger Tests Saga Orchestration', () => {
  const host = Cypress.env('host') ? Cypress.env('host') : 'localhost';
  const port = Cypress.env('port') ? Cypress.env('port') : '8082';
  const baseUrl = Cypress.env('port')  ? '/api/saga/orchestration' : '/api/saga';
  const docPath = Cypress.env('port') ? '/api/saga/orchestration/v3/api-docs' : '/api/saga/v3/api-docs';

  it('shows swagger', () => {
    cy.visit(`http://${host}:${port}${baseUrl}/swagger-ui/index.html`);
    cy.get('input[class="download-url-input"]').clear().type(docPath);
    cy.get('button').contains('Explore').click();
    cy.get('h2', {timeout:10000}).contains('OpenAPI definition', {timeout: 10000}).should('not.be.null');
    cy.wait(1000);

    cy.get('div[class="servers"] > label > select > option').should('have.value', 'http://localhost:9000/api/saga/orchestration')
  });

})
