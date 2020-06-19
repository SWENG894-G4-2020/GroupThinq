/* eslint-disable */
/**
 * @jest-environment jsdom
 */


import auth from 'src/store/auth'
import { LocalStorage } from 'quasar'

describe('Auth Util tests', () => {

    beforeEach(() => {
        jest.resetAllMocks()
    })

    it('stores an included token', () => {
        LocalStorage.set = jest.fn() // set up a mock for LocalStorage
        auth.storeToken(`test.${btoa('{"test":"test"}')}`) // set up a test token
        
        // assert that LocalStorage would have been callled correctly
        expect(LocalStorage.set).toHaveBeenCalledWith('encodedToken',`test.${btoa('{"test":"test"}')}`)
        expect(LocalStorage.set).toHaveBeenCalledWith('tokenData',{"test":"test"})
    })

    it('handles not having a token', () => {
        LocalStorage.set = jest.fn() // set up a mock for LocalStorage

        // assert that storeToken() will thrown an error if not given an argument
        expect(() => auth.storeToken()).toThrow('No token provided')
    })

    it('sets headers', () => {
        LocalStorage.get = jest.fn(() => ('test'))
        let configTest = {headers: {Authorization: ''}}

        auth.setHeaders(configTest)

        expect(configTest.headers.Authorization).toBe("Bearer test")
    })

    it('gets an encoded token', () => {
        LocalStorage.getItem = jest.fn(() => ('test'))
        auth.getEncodedToken()
        expect(LocalStorage.getItem).toHaveBeenCalledWith('encodedToken')
    })

    it('gets token data', () => {
        LocalStorage.getItem = jest.fn(() => ('test'))
        auth.getTokenData()
        expect(LocalStorage.getItem).toHaveBeenCalledWith('tokenData')
       
    })

    it('checks if user is logged in', () => {
        LocalStorage.getItem = jest.fn(() => ('test'))
        expect(auth.isLoggedIn()).toBe(true)
    })

    it('checks if user is logged out', () => {
        LocalStorage.getItem = jest.fn(() => (''))
        expect(auth.isLoggedIn()).toBe(false)
    })

    it('remotes tokens', () => {
        LocalStorage.remove = jest.fn()
        auth.removeTokens()
        expect(LocalStorage.remove).toHaveBeenCalledWith('encodedToken')
        expect(LocalStorage.remove).toHaveBeenCalledWith('tokenData')
    })
})
