
Cypress.Commands.add('loginAdmin', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);        
        cy.get('[ng-reflect-text="login"]').within(() => {
            cy.get('a').click();
        })
        cy.get('input[name="username"]').type(settings.adminUser);
        cy.get('input[name="password"]').type(settings.adminPw);
        cy.contains('button', 'Anmelden').click();
    })
})

Cypress.Commands.add('loginUser', () => {
    cy.fixture('settings').then(settings => {
        cy.visit(settings.baseUrl);
        cy.get('[ng-reflect-text="login"]').within(() => {
            cy.get('a').click();
        })
        cy.get('input[name="username"]').type(settings.user);
        cy.get('input[name="password"]').type(settings.pw);
        cy.contains('button', 'Anmelden').click();
    })
})