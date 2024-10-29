/// <reference types="cypress" />

describe('Users List', () => {
    it('login as admin', () => {
      cy.visit('/auth/login');
  
      cy.intercept('POST', '/api/auth/authentication', (req) => {
        req.reply({
          statusCode: 200,
          body: {
            token: 'dummy-token'
          }
        });
      }).as('loginRequest');
  
      cy.intercept('GET', '/api/auth/me', {
        statusCode: 200,
        body: {
          id: 1,
          email: 'test@mail.com',
          pseudo: 'admintest',
          roles: ['ADMIN'],
          firstname: 'John',
          lastname: 'Doe',
          dateOfBirth: '2000-01-01'
        }
      }).as('getUser');
  
      cy.intercept('GET', '/api/admin/users', [
        {
          id: 1,
          email: 'test1@mail.com',
          pseudo: 'test1',
          roles: ['USER'],
          firstname: 'John',
          lastname: 'Doe',
          dateOfBirth: '2000-01-01'
        },
        {
          id: 2,
          email: 'test2@mail.com',
          pseudo: 'test2',
          roles: ['USER'],
          firstname: 'Mike',
          lastname: 'Ross',
          dateOfBirth: '2000-01-01'
        }
      ]).as('users');
      cy.get('#list').should('exist').click();
    
      // Vérifie que l'URL a bien changé
      cy.url().should('include', '/admin/users');
      cy.contains("test1");
      cy.contains("test2");
    });
  });
  