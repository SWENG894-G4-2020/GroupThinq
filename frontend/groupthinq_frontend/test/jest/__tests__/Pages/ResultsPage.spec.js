/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Results from 'src/pages/Results'
import ResultsCard from 'src/components/ResultsCard'
import axios from 'axios'
import auth from 'src/store/auth'
import * as All from 'quasar'

// add all of the Quasar objects to the test harness
const { Quasar, date } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

const testData = { data: { data: [
  {
    id: 4,
    name: "Title",
    description: "Description",
    ownerUsername: "test",
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
          }
        ],
        createdDate: "2020-07-19T02:02:31.006Z",
        decisionId: 0,
        expirationDate: "2020-07-19T02:02:31.006Z",
        id: 0,
        updatedDate: "2020-07-19T02:02:31.006Z"
      }
    ],
    includedUsers: [{userName: 'test'}]
  },
  {
    id: 5,
    name: "AnotherTitle",
    description: "AnotherDescription",
    ownerUsername: "testUser",
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
          }
        ],
        createdDate: "2020-07-19T02:02:31.006Z",
        decisionId: 0,
        expirationDate: "2020-07-19T02:02:31.006Z",
        id: 0,
        updatedDate: "2020-07-19T02:02:31.006Z"
      }
    ],
    includedUsers: [{userName: 'testUser'}]
  }
]}}

const resetData = {
  name: '',
  description: '',
  ownerUsername: 'test',
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
        }
      ],
      createdDate: "2020-07-19T02:02:31.006Z",
      decisionId: 0,
      expirationDate: "2020-07-19T02:02:31.006Z",
      id: 0,
      updatedDate: "2020-07-19T02:02:31.006Z"
    }
  ],
  includedUsers: [{userName: 'test'}]
}

describe('Results page tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  beforeEach( () => {
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('contains a Results card after initial data fetch', async () => {
    // set up the Axios mock
    axios.get = jest.fn(() => Promise.resolve(testData))
    localVue.prototype.$axios = axios

    // mount the component under test
    const wrapper = shallowMount(Results, { localVue })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.isLoaded).toBe(true)
    expect(wrapper.findComponent(ResultsCard).exists()).toBe(true)
  })
  
  it('catches axios get errors', async () => {
    console.log = jest.fn()
    axios.get = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(Results, { localVue })
    const vm = wrapper.vm
    expect(vm.isError).toBe(false)
    await localVue.nextTick() // wait for the mounted function to finish
    //expect(console.log).toHaveBeenCalledWith('Test Error')
    expect(vm.isError).toBe(true)
  })

})
