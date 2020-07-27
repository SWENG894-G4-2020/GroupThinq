/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import DecisionSummaryCard from 'src/components/DecisionSummaryCard'
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

describe('Decision Summary Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  const testPropsTemplate = {
    decision: {
      id: 5,
      name: "Decision 1",
      description: "Decision with Ballot and 4 Ballot Options",
      ownerUsername: "test",
      includedUsers: [
          {userName: "test"},
          {userName: "test2"},
          {userName: "test3"},
          {userName: "test4"},
          {userName: "test5"},
          {userName: "test6"},
          {userName: "test7"},
          {userName: "test8"},
          {userName: "test9"},
          {userName: "test10"},
          {userName: "test11"}
      ],
      ballots: [{
        id: 10,
        expirationDate: "2020-10-10T04:00:00.000Z",
        ballotTypeId: 1,
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
  }
  var testProps = {}

  beforeEach( () => {
    testProps = JSON.parse(JSON.stringify(testPropsTemplate))
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('calculates expiration time correctly', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-01T09:26:00-04:00'))
    const wrapper = shallowMount(DecisionSummaryCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.calculateRemainingTime()
    jest.advanceTimersByTime(1001)
    expect(Date.now).toHaveBeenCalled()
    expect(vm.isOpen).toBeTruthy()
  })

  it('exit expiration calculation when expired', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-11-11T09:26:00-04:00'))
    const wrapper = shallowMount(DecisionSummaryCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.calculateRemainingTime()
    jest.advanceTimersByTime(1001)
    expect(Date.now).toHaveBeenCalled()
    expect(vm.isOpen).toBeFalsy()
  })

  it('truncates the numver of users to display when over 9', async () => {
    expect(DecisionSummaryCard.computed.truncatedUsers
      .call(testProps))
      .toHaveLength(9)
  })

  it('does not truncate displayed users under 9', async () => {
    testProps.decision.includedUsers = [
      {userName: "test"},
      {userName: "test2"},
      {userName: "test3"},
      {userName: "test4"},
      {userName: "test5"}
    ]
    expect(DecisionSummaryCard.computed.truncatedUsers
      .call(testProps))
      .toHaveLength(5)
  })

  it('returns 0 users over when under 9', async () => {
    testProps.decision.includedUsers = [
      {userName: "test"},
      {userName: "test2"},
      {userName: "test3"},
      {userName: "test4"},
      {userName: "test5"}
    ]
    expect(DecisionSummaryCard.computed.overUsers
      .call(testProps))
      .toStrictEqual(0)
  })
})
