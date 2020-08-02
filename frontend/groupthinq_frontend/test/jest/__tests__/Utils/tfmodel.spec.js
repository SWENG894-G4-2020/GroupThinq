/* eslint-disable */
/**
 * @jest-environment jsdom
 */


import tfmodel from 'src/store/tfmodel'

describe('TensorFlow ML Model Tests', () => {

    beforeEach(() => {
        jest.resetAllMocks()
    })

    it('runs the ML with normal inputs', async () => {

        jest.setTimeout(20000)
        tfmodel.setModelBackend('cpu')
        let results = await tfmodel.getSimilarityScores(['foo', 'bar', 'baz'])
        
        expect(results.length).toBe(2)
    })
})
