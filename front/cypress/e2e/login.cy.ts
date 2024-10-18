describe('Login spec', () => {
    it('login successfully', () => {
        cy.visit('/auth/login');

        cy.intercept('POST', '/api/auth/login', {
            body: {
                email: 'email',
                password: 'password'
            },
        }).as('loginRequest');
        cy.get('input[formControlName=email]').type('test@mail.fr');
        cy.get('input[formControlName=password]').type('pasword-test');
        cy.get('button').contains('Se connecter').click();
       

        cy.url().should('include', '/reception/welcome');

    });
});