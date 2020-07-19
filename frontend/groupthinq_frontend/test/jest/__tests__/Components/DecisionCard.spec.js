/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import DecisionCard from 'src/components/DecisionCard'
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

describe('Decision Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

  const testPropsTemplate = {
    id: 5,
    name: "Decision 1",
    description: "Decision with Ballot and 4 Ballot Options",
    ownerUsername: "test",
    includedUsers: [
        {userName: "test"},
        {userName: "test2"}
    ],
    ballots: [{
      id: 10,
      expirationDate: "2020-10-10T04:00:00.000Z",
      ballotOptions: [
        {
          title: "Ballot Option 1",
          userName: "testuser1",
          description: "This is a description for Ballot Option 1"
        },
        {
            title: "Ballot Option 2",
            userName: "testuser1",
            description: "This is a description for Ballot Option 2"
        }
            ]
        }
    ]
  }
  var testProps = {}

  const testData = { data: { data: [
    {
      ballotId: 1,
      ballotOptionId: 1,
      userName: "testuser1",
      voteDate: "2020-07-17T20:11:47.334+00:00",
      voteUpdatedDate: null
    },
    {
      ballotId: 1,
      ballotOptionId: 2,
      userName: "testUser",
      voteDate: "2020-07-17T20:11:59.481+00:00",
      voteUpdatedDate: null
    }]}}

  beforeEach( () => {
    testProps = JSON.parse(JSON.stringify(testPropsTemplate))
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('calculates expiration time correctly', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-01T09:26:00-04:00'))
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.calculateRemainingTime()
    jest.advanceTimersByTime(1001)
    expect(Date.now).toHaveBeenCalled()
  })

  it('exit expiration calculation when expired', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-11-11T09:26:00-04:00'))
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.calculateRemainingTime()
    jest.advanceTimersByTime(1001)
    expect(Date.now).toHaveBeenCalled()
  })

  it('determines the status for an expired vote', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-02T09:26:00-04:00'))
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    expect(DecisionCard.computed.expired
      .call({editableDecision: {ballots: [{epirationDate: "2020-10-10T04:00:00.000Z"}]}}))
      .toBe(false)
  })

  it('determines previous vote status', async () => {

    expect(DecisionCard.computed.previousVote
      .call({
        currentUserName: 'testUser', 
        resultsList: testData.data.data,
        ballots: [{
          ballotOptions: [
            {
              id: 1,
              title: "Ballot Option 1",
              userName: "testuser1",
              description: "This is a description for Ballot Option 1"
            },
            {
              id: 2,
                title: "Ballot Option 2",
                userName: "testuser1",
                description: "This is a description for Ballot Option 2"
            }
                ]
            }
        ]
      }))
      .toStrictEqual({title: "Ballot Option 2", description: "This is a description for Ballot Option 2"})
  })

  it('determines correct vote label', async () => {

    expect(DecisionCard.computed.voteLabel
      .call({currentUserName: 'testUser', resultsList: [{userName: 'testUser'}]}))
      .toBe('View Vote')

    expect(DecisionCard.computed.voteLabel
      .call({currentUserName: 'test', resultsList: [{userName: 'testUser'}]}))
      .toBe('Vote')
  })

  it('gets results data on mount', async () => {
    axios.get = jest.fn(() => Promise.resolve(testData))
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    // no need for getResultsData() call since it occurs on mount
    
    expect(axios.get).toHaveBeenCalledTimes(1)
  })

  it('uses a default users list when none is provided', async () => {
    let tempTestProps = testProps
    tempTestProps.includedUsers = undefined
    const wrapper = shallowMount(DecisionCard, { propsData: tempTestProps, localVue })
    const vm = wrapper.vm
    expect(vm.$props.includedUsers).toStrictEqual([{ userName: 'test'}])
  })

  it('deletes a decision', async () => {
    axios.delete = jest.fn(() => Promise.resolve())
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.onConfirmDelete()
    expect(axios.delete).toHaveBeenCalledTimes(1)
    expect(vm.$data.deleteDecisionDialog).toBe(false)
  })

  it('opens and closes the edit modal', async () => {
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.openEditModal()
    expect(vm.editDecisionDialog).toBeTruthy()
    await vm.closeEditModal()
    expect(vm.editDecisionDialog).toBeFalsy()
  })

  it('closes the voting modal', async () => {
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    vm.$data.votingDialog = true
    await vm.closeVotingModal()
    expect(vm.votingDialog).toBeFalsy()
  })

  it('catches axios errors', async () => {
    axios.get= jest.fn(() => Promise.reject('Test Axios Error'))
    axios.delete= jest.fn(() => Promise.reject('Test Axios Error'))
    console.log = jest.fn()
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.onConfirmDelete()
    // no need for getResultsData() call since it occurs when mounted
    
    expect(console.log).toHaveBeenCalledWith('Test Axios Error')
    expect(console.log).toHaveBeenCalledTimes(2)
  })

  it('handles zero length prop inputs', async () => {
    const wrapper = shallowMount(DecisionCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
  })
})