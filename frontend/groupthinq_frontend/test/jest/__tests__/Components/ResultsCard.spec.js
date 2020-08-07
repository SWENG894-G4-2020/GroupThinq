/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import ResultsCard from 'src/components/ResultsCard'
import axios from 'axios'
import auth from 'src/store/auth'
import * as All from 'quasar'

// add all of the Quasar objects to the test harness
const { Quasar, date, ClosePopup } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Results Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

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

  const testData = { data: { data: [
    {
      ballotId: 0,
      ballotOptionId: 0,
      userName: "testuser",
      voteDate: "2020-07-17T20:11:47.334+00:00",
      voteUpdatedDate: null,
      id: 1,
      rank: 1
    },
    {
      ballotId: 0,
      ballotOptionId: 1,
      userName: "testuser",
      voteDate: "2020-07-17T20:11:59.481+00:00",
      voteUpdatedDate: null,
      id: 2,
      rank: 2
    },
    {
      ballotId: 0,
      ballotOptionId: 2,
      userName: "testuser",
      voteDate: "2020-07-17T20:11:59.481+00:00",
      voteUpdatedDate: null,
      id: 3,
      rank: 3
    }]}}

    const testRankedResults = { data: 
      {
        success:true,
        status:200,
        data: [{
          ballotId:0,
          winner:{
            ballotId: 0,
            createdDate: "2020-07-19T02:02:31.006Z",
            description: "string",
            id: 0,
            title: "string",
            updatedDate: "2020-07-19T02:02:31.006Z",
            userName: "string"
          },
          rankedPairWinners:[
          {
            winner:{
              ballotId: 0,
              createdDate: "2020-07-19T02:02:31.006Z",
              description: "string",
              id: 0,
              title: "string",
              updatedDate: "2020-07-19T02:02:31.006Z",
              userName: "string"
            },
            loser: {
              ballotId: 0,
              createdDate: "2020-07-19T02:02:31.006Z",
              description: "string",
              id: 1,
              title: "string",
              updatedDate: "2020-07-19T02:02:31.006Z",
              userName: "string"
            },
            margin:1
          },
          {
            winner: {
              ballotId: 0,
              createdDate: "2020-07-19T02:02:31.006Z",
              description: "string",
              id: 0,
              title: "string",
              updatedDate: "2020-07-19T02:02:31.006Z",
              userName: "string"
            },
            loser: {
              ballotId: 0,
              createdDate: "2020-07-19T02:02:31.006Z",
              description: "string",
              id: 2,
              title: "string",
              updatedDate: "2020-07-19T02:02:31.006Z",
              userName: "string"
            },
            margin:1
            },
            {
              winner:{
                ballotId: 0,
                createdDate: "2020-07-19T02:02:31.006Z",
                description: "string",
                id: 1,
                title: "string",
                updatedDate: "2020-07-19T02:02:31.006Z",
                userName: "string"
              },
              loser:{
                ballotId: 0,
                createdDate: "2020-07-19T02:02:31.006Z",
                description: "string",
                id: 2,
                title: "string",
                updatedDate: "2020-07-19T02:02:31.006Z",
                userName: "string"
              },
              margin:1
              }
            ]
          }
          ],
            errors:[]
          }
    }
    
  beforeEach( () => {
    testProps = JSON.parse(JSON.stringify(testPropsTemplate))
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('gets results data on mount', async () => {
    axios.get = jest.fn(() => Promise.resolve(testData))
    const wrapper = shallowMount(ResultsCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    // no need for getResultsData() call since it occurs on mount
    
    expect(axios.get).toHaveBeenCalledTimes(1)
  })

  it('displays the correct table depending on ballot type', async () => {
    // set up the Axios mock
    axios.get = jest.fn(() => Promise.resolve(testRankedResults))
    localVue.prototype.$axios = axios
    testProps.decision.ballots[0].ballotTypeId = 2
    // mount the component under test
    const wrapper = shallowMount(ResultsCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.isLoaded).toBe(true)
    expect(vm.rankedPairResults)
      .toHaveLength(3)
  })

  it('catches axios errors', async () => {
    axios.get= jest.fn(() => Promise.reject('Test Axios Error'))
    console.log = jest.fn()

    const wrapper = shallowMount(ResultsCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(console.log).toHaveBeenCalledWith('Test Axios Error')
    expect(console.log).toHaveBeenCalledTimes(1)
  })

  
})