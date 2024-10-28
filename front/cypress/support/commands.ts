/// <reference types="cypress" />

declare namespace Cypress {
    interface Chainable<Subject = any> {
      login(email: string, password: string): typeof login;
    }
 }

function login(email: string, password: string): void {
    cy.visit('/auth/login');

    cy.intercept('POST', '/api/auth/authentication', {
        body: {
            email: 'email',
            password: 'password'
        },
    }).as('loginRequest');
    cy.intercept(
        {
          method: 'GET',
          url: '/api/auth/me',
        },  {
            id: 1,
            email: 'test@mail.com',
            pseudo: 'testuser',
            roles: ['USER'],
            firstname: 'John',
            lastname: 'Doe',
            dateOfBirth: '2000-01-01'
          },
    
    ).as('user')
    cy.get('input[formControlName=email]').type('test@mail.fr');
    cy.get('input[formControlName=password]').type('pasword-test');
    cy.get('button').contains('Se connecter').click();
}
Cypress.Commands.add('login', login);
