/* eslint-disable */
/**
 * @jest-environment jsdom
 */


import routerSetup from 'src/router/index'

describe('Router Index tests', () => {

    it('sets up a router', () => {
        let testRouter = routerSetup()
        expect(testRouter).toBeTruthy()
    })
})
