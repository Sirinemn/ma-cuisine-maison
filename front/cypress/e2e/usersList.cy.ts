/// <reference types="cypress" />

describe('Users List', () => {
    it('login as admin', () => {
      // Visite la page de login
      cy.visit('/auth/login');
  
      // Intercepte la requête de login
      cy.intercept('POST', '/api/auth/authentication', (req) => {
        req.reply({
          statusCode: 200,
          body: {
            token: 'dummy-token'
          }
        });
      }).as('loginRequest');
  
      // Intercepte la requête pour obtenir les infos de l'utilisateur
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

  
      cy.get('#list').should('exist').click();
  
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
    
      // Vérifie que l'URL a bien changé
      cy.url().should('include', '/admin/users');
    });
  });
  