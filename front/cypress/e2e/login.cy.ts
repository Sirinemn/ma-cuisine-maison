describe('Login spec', () => {
    it('login successfully', () => {
       cy.login('test@mail.fr','passwordTest')

        cy.url().should('include', '/reception/welcome');

    });
    it('If admin should contain userslist', () => {
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
                pseudo: 'admintest',
                roles: ['ADMIN'],
                firstname: 'John',
                lastname: 'Doe',
                dateOfBirth: '2000-01-01'
              },
        
        ).as('user')
        cy.get('input[formControlName=email]').type('admin@mail.fr');
        cy.get('input[formControlName=password]').type('pasword');
        cy.get('button').contains('Se connecter').click();
       
        cy.contains('Liste des utilisateurs')
        cy.url().should('include', '/reception/welcome');

    })
});