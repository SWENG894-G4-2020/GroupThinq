/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import BrainCard from 'src/components/BrainCard'
import axios from 'axios'
import auth from 'src/store/auth'
import tfmodel from 'src/store/tfmodel'
import * as All from 'quasar'
import { exp } from '@tensorflow/tfjs'

// add all of the Quasar objects to the test harness
const { Quasar, date } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Edit Decision Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }, tfmodel) // , lang: langEn

  const testPropsTemplate = {
    decision: {
      id: 5,
      name: "Decision 1",
      description: "Decision with Ballot and 4 Ballot Options",
      ownerUsername: "testUser",
      includedUsers: [
          {userName: "test"},
          {userName: "test2"}
      ],
      ballots: [
        {
        ballotOptions: [
          {
            ballotId: 0,
            createdDate: "2020-07-19T02:02:31.006Z",
            description: "string",
            id: 0,
            title: "string",
            updatedDate: "2020-07-19T02:02:31.006Z",
            userName: "string"
          },
          {
            ballotId: 0,
            createdDate: "2020-07-19T02:02:31.006Z",
            description: "string",
            id: 1,
            title: "string",
            updatedDate: "2020-07-19T02:02:31.006Z",
            userName: "string"
          },
          {
            ballotId: 0,
            createdDate: "2020-07-19T02:02:31.006Z",
            description: "string",
            id: 2,
            title: "string",
            updatedDate: "2020-07-19T02:02:31.006Z",
            userName: "string"
          }
        ],
        ballotTypeId: 1,
        createdDate: "2020-07-19T02:02:31.006Z",
        decisionId: 0,
        expirationDate: "2020-07-19T02:02:31.006Z",
        id: 0,
        updatedDate: "2020-07-19T02:02:31.006Z"
      }
      ]
    }
  }

  var testProps = {}

  beforeEach( () => {
    testProps = JSON.parse(JSON.stringify(testPropsTemplate))
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('catches axios get errors', async () => {
    console.log = jest.fn()
    axios.get = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(BrainCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    await localVue.nextTick()// wait for the mounted function to finish
    expect(console.log).toHaveBeenCalledWith('Test Error')

  })

  it('gets the correct number of similarity scores back', async () => {
    tfmodel.getSimilarityScores = jest.fn(() => Promise.resolve([0.15, 0.25, 0.35]))
    axios.get = jest.fn(() => Promise.reject({ data: { data: testProps.decision } }))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(BrainCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.getML({ id: 5 })
    expect(axios.get).toHaveBeenCalled()
  })

})
