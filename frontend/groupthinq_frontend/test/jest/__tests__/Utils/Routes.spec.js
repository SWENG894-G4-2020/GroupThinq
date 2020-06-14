/* eslint-disable */
/**
 * @jest-environment jsdom
 */


import routes from 'src/router/routes'

describe('Router Route tests', () => {

    it('contains the expected routes', () => {
        routes.forEach(route => {
            route.component()
            expect(route.path).toBeTruthy()
            if (route.children) {
                route.children.forEach(child => {
                    child.component()
                    expect(child).toBeTruthy()
                })
            }
        })
        expect(routes.length).toBe(3)
    })
    
})
