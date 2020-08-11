/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Index from 'src/pages/Index'
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

const testData = { data: { data: [
  {
    id: 4,
    name: "Title",
    description: "Description",
    ownerUsername: "test",
    deleted: false,
    ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
    includedUsers: [{userName: 'test'}]
  },
  {
    id: 5,
    name: "AnotherTitle",
    description: "AnotherDescription",
    ownerUsername: "testUser",
    deleted: true,
    ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
    includedUsers: [{userName: 'testUser'}]
  },
  {
    id: 6,
    name: "AnotherTitle",
    description: "AnotherDescription",
    ownerUsername: "testUser",
    deleted: false,
    ballots: [{expirationDate: '2020-12-12T09:26:00-04:00'}],
    includedUsers: [{userName: 'testUser'}]
  },
  {
    id: 7,
    name: "ExpiredTitle",
    description: "AnotherDescription",
    ownerUsername: "testUser",
    deleted: false,
    ballots: [{expirationDate: '2010-12-12T09:26:00-04:00'}],
    includedUsers: [{userName: 'testUser'}]
  },
  {
    id: 8,
    name: "ExpiredTitleAgain",
    description: "AnotherDescription",
    ownerUsername: "testUser",
    deleted: false,
    ballots: [{expirationDate: '2010-11-12T09:26:00-04:00'}],
    includedUsers: [{userName: 'testUser'}]
  }
]}}

const resetData = {
  name: '',
  description: '',
  ownerUsername: 'test',
  ballots: [{expirationDate: '2019-09-02T09:26:00-04:00'}],
  includedUsers: [{userName: 'test'}]
}

describe('Decisions page tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  beforeEach( () => {
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('contains a Decision summary card after initial data fetch', async () => {
    // set up the Axios mock
    axios.get = jest.fn(() => Promise.resolve(testData))
    localVue.prototype.$axios = axios

    // mount the component under test
    const wrapper = shallowMount(Index, { localVue })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.isLoaded).toBe(true)
    expect(wrapper.findComponent(DecisionSummaryCard).exists()).toBe(true)
  })
  
  it('catches axios get errors', async () => {
    console.log = jest.fn()
    axios.get = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(Index, { localVue })
    const vm = wrapper.vm
    expect(vm.isError).toBe(false)
    await localVue.nextTick() // wait for the mounted function to finish
    //expect(console.log).toHaveBeenCalledWith('Test Error')
    expect(vm.isError).toBe(true)
  })

  it('changes sorts correctly', async () => {
   // jest.useFakeTimers()
    //Date.now = jest.fn(() => new Date('2020-11-11T09:26:00-04:00'))
    axios.get = jest.fn(() => Promise.resolve(testData))
    const wrapper = shallowMount(Index, { localVue })
    const vm = wrapper.vm
    //jest.advanceTimersByTime(1001)

    await localVue.nextTick()
    vm.$data.currentSort = { label: 'Upcoming Votes', value: 'upcoming' }
    await localVue.nextTick()
    vm.$data.currentSort = { label: 'Recently Closed', value: 'recent' }
    await localVue.nextTick()
    vm.$data.currentSort = { label: 'My Decisions', value: 'mine' }
    await localVue.nextTick()
  })

  it('filters results properly', async () => {
    axios.get = jest.fn(() => Promise.resolve(testData))
    const wrapper = shallowMount(Index, { localVue })
    const vm = wrapper.vm
    
    await localVue.nextTick()
    vm.$data.search = 'anoth'
    await localVue.nextTick()
    expect(vm.filteredDecisions)
      .toHaveLength(2)
  })

  it('filters expired decisions', async () => {
    axios.get = jest.fn(() => Promise.resolve(testData))
    const wrapper = shallowMount(Index, { localVue })
    const vm = wrapper.vm
    
    await localVue.nextTick()
    vm.$data.search = 'expir'
    await localVue.nextTick()
    expect(vm.filteredDecisions)
      .toHaveLength(2)
    vm.$data.currentSort.value = 'badtest'
    await localVue.nextTick()
  })

  it('calls the correct endpoint when an admin', async () => {
    axios.get = jest.fn(() => Promise.resolve(testData))
    auth.getTokenData = jest.fn(() => ({sub: 'testUser', role: 'Admin'}))
    const wrapper = shallowMount(Index, { localVue })
    const vm = wrapper.vm
    
    await localVue.nextTick()

    expect(vm.$data.currentUserRole).toBe('Admin')
  })

})
