/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import BallotCard from 'src/components/BallotCard'
import axios from 'axios'
import auth from 'src/store/auth'
import * as All from 'quasar'
import { exp } from '@tensorflow/tfjs'

// add all of the Quasar objects to the test harness
const { Quasar, date, ClosePopup } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Ballot Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

  const testPropsTemplate = {
    mode: 'view',
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

  const testVotesRanked = { data: { data: [
    {
      ballotId: 0,
      ballotOptionId: 0,
      userName: "testUser",
      voteDate: "2020-07-17T20:11:47.334+00:00",
      voteUpdatedDate: null,
      id: 1,
      rank: 1
    },
    {
      ballotId: 0,
      ballotOptionId: 1,
      userName: "testUser",
      voteDate: "2020-07-17T20:11:59.481+00:00",
      voteUpdatedDate: null,
      id: 2,
      rank: 2
    },
    {
      ballotId: 0,
      ballotOptionId: 2,
      userName: "testUser",
      voteDate: "2020-07-17T20:11:59.481+00:00",
      voteUpdatedDate: null,
      id: 3,
      rank: 3
    }
  ]
}
}

const testVotesNormal= { data: { data: [
  {
    ballotId: 0,
    ballotOptionId: 0,
    userName: "testUser",
    voteDate: "2020-07-17T20:11:47.334+00:00",
    voteUpdatedDate: null,
    id: 1,
    rank: 1
  },
  {
    ballotId: 0,
    ballotOptionId: 0,
    userName: "testUser1",
    voteDate: "2020-07-17T20:11:59.481+00:00",
    voteUpdatedDate: null,
    id: 2,
    rank: 2
  },
  {
    ballotId: 0,
    ballotOptionId: 1,
    userName: "testUser2",
    voteDate: "2020-07-17T20:11:59.481+00:00",
    voteUpdatedDate: null,
    id: 3,
    rank: 3
  }
]
}
}

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

  it('populates ballot when supplied a decision', async () => {
    axios.get = jest.fn(() => Promise.resolve(testVotesNormal))
    const wrapper = shallowMount(BallotCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.$nextTick()
    expect(axios.get).toHaveBeenCalledTimes(1)
    expect(vm.$data.ballotTypeId).toBe(1)
    expect(vm.$data.options).toHaveLength(3)
  })

  it('retrieves previous votes', async () => {
    axios.get = jest.fn(() => Promise.resolve(testVotesNormal))
    const wrapper = shallowMount(BallotCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.getVotes()
    expect(vm.$data.newVoteSelection).toBe(0)
  })

  it('creates a new vote object on voting start', async () => {
    axios.get = jest.fn(() => Promise.resolve(testVotesNormal))
    const wrapper = shallowMount(BallotCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.$nextTick()
    vm.onVoteStart()
    expect(vm.$data.voting).toBe(true)
    expect(vm.$data.newVote).toHaveLength(3)
    expect(vm.$data.newVote[0].vote).toBe(true)
  })

  it('will not confirm vote with no selection', async () => {
    const modResults = JSON.parse(JSON.stringify(testVotesNormal))
    modResults.data.data[0].userName = 'testuser2'
    axios.get = jest.fn(() => Promise.resolve(modResults))
    const wrapper = shallowMount(BallotCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.$nextTick()
    await vm.onVoteConfirm()
    expect(vm.$data.voting).toBe(false)
  })

  it('performs POST when no previous vote', async () => {
    const modResults = JSON.parse(JSON.stringify(testVotesNormal))
    modResults.data.data[0].userName = 'testuser2'
    axios.get = jest.fn(() => Promise.resolve(modResults))
    axios.post = jest.fn(() => Promise.resolve())
    const wrapper = shallowMount(BallotCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.$nextTick()
    vm.$data.newVoteSelection = 0
    await vm.onVoteConfirm()
    expect(vm.$data.voting).toBe(false)
    expect(axios.post).toHaveBeenCalledTimes(1)
  })

  it('performs PUT on previous vote', async () => {
    testProps.decision.ballots[0].ballotTypeId = 2
    axios.get = jest.fn(() => Promise.resolve(testVotesRanked))
    axios.put = jest.fn(() => Promise.resolve())
    const wrapper = shallowMount(BallotCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.$nextTick()
    vm.$data.newVoteSelection = 1
    await vm.onVoteConfirm()
    expect(vm.$data.voting).toBe(false)
    expect(axios.put).toHaveBeenCalledTimes(1)
  })

  it('catches axios errors when submitting', async () => {
    axios.put= jest.fn(() => Promise.reject('Test Axios Error'))
    console.log = jest.fn()
    const wrapper = shallowMount(BallotCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.$nextTick()
    vm.$data.newVoteSelection = 0
    await vm.onVoteConfirm()
    // no need for getResultsData() call since it occurs when mounted
    
    expect(console.log).toHaveBeenCalledWith('Test Axios Error')
    expect(console.log).toHaveBeenCalledTimes(1)
  })

  it('adds a decision in view mode', async () => {
    axios.get = jest.fn(() => Promise.resolve(testVotesNormal))
    const wrapper = shallowMount(BallotCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    vm.addBallotOption = jest.fn(() => Promise.resolve())
    wrapper.setData({newOption: {
      title: 'Added title'
    }})
    await vm.$nextTick()
    await vm.addDecisionOption()
    expect(vm.addBallotOption).toBeCalled()
  })

  it('adds a decision in create mode', async () => {
    const wrapper = shallowMount(BallotCard, { propsData: { mode: 'create' }, localVue })
    const vm = wrapper.vm
    wrapper.setData({newOption: {
      title: 'Added title'
    }})
    await vm.addDecisionOption()
    expect(vm.options).toHaveLength(1)
  })

  it('removes a decision in create mode', async () => {
    const wrapper = shallowMount(BallotCard, { propsData: { mode: 'create' }, localVue })
    const vm = wrapper.vm
    wrapper.setData({
      options: [
      {
        title: 'Added title',
        userName: 'test'
      }
    ]})
    expect(vm.options).toHaveLength(1)
    vm.removeDecisionOption ({
      title: 'Added title',
      userName: 'test'
    })
    expect(vm.options).toHaveLength(0)
  })

})
