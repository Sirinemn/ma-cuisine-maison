describe('User Profile', () => {
    beforeEach(() => {

        cy.login('test@mail.fr','passwordTest')
      
      })

    it('User information', () => {
        cy.get("#profile").click();
        cy.intercept({
            method: 'GET',
            url: 'api/user/1'
        },{
            
            id: 1,
            email: 'test@mail.com',
            pseudo: 'pseudo',
            roles: ['USER'],
            firstname: 'John',
            lastname: 'Doe',
            dateOfBirth: '2000-01-01'
            
        }).as('user')
        cy.url().should('include', '/me');
    })
})