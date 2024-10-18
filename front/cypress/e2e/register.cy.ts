describe('Register spec', () =>{
    it('Register successfully', () => {
        cy.visit('/auth/register');

        cy.intercept('POST', '/api/auth/register', {
            body: {
                email: 'email',
                firstName: 'firstName',
                lastName: 'lastName',
                password: 'password',
                pseudo: 'pseudo',
                dateOfBirth: 'dateOfBirth'
            },
        });
        cy.get('input[formControlName=firstname]').type("first");
        cy.get('input[formControlName=lastname]').type("last");
        cy.get('input[formControlName=pseudo]').type("pseudo");
        cy.get('input[formControlName=password]').type("password-test");
        cy.get('[formControlName="dateOfBirth"]').type('01/01/1990');
        cy.get('input[formControlName=email]').type(`${'test@mail.fr'}{enter}{enter}`);

        cy.url().should('include', '/activate-account');

        cy.pause();
    });
    it('Acticate account', () => {
        cy.visit('/auth/activate-account');

        cy.get('code-input').within(() => {
            cy.get('input').eq(0).type('1');
            cy.get('input').eq(1).type('2');
            cy.get('input').eq(2).type('3');
            cy.get('input').eq(3).type('4');
            cy.get('input').eq(4).type('5');
            cy.get('input').eq(5).type('6');
          });
        cy.get('button').contains('Go to Login').click()

        cy.url().should('include', '/auth/login');
    })

});