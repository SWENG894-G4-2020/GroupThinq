/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import EditDecisionCard from 'src/components/EditDecisionCard'
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

describe('Edit Decision Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  beforeEach( () => {
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('catches axios get errors', async () => {
    console.log = jest.fn()
    axios.get = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    await localVue.nextTick()// wait for the mounted function to finish
    expect(console.log).toHaveBeenCalledWith('Test Error')

  })

  it('catches axios put errors', async () => {
    console.log = jest.fn()
    axios.put = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    await vm.onEditConfirm() // wait for the mounted function to finish
    expect(console.log).toHaveBeenCalledWith('Test Error')
  })

  it('gets all users', async () => {
    axios.get = jest.fn(() => Promise.resolve({data: { data: [{ userName: 'test1' }, { userName: 'test2' }]}}))
    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm
    
    await localVue.nextTick()
    expect(axios.get).toHaveBeenCalledTimes(1)
    expect(vm.$data.allUsersList).toStrictEqual(['test1', 'test2'])
  })

  it('edits a decision', async () => {
    axios.put = jest.fn()
    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    await vm.onEditConfirm()
    expect(axios.put).toHaveBeenCalledTimes(1)
  })

  it('emits a cancel event on cancel', async () => {
    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    await vm.onCancel()
    expect(wrapper.emitted().editClose).toBeTruthy()
  })

  it('filters user hints', async () => {
    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    vm.$data.allUsersList = ['test1', 'test2']
    await vm.filterFn('test', (fn) => (fn()), () => {})
    await vm.filterFn('t', (fn) => {fn()}, () => {})
  })

  it('can reset the edited decision data', async () => {
    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    await vm.fillEditableDecision()
  })

  it('can add a user to the addedUsers list', async () => {
    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    vm.$data.newIncludedUser = 'test1'
    await vm.addIncludedUser()
    expect(vm.$data.editableDecision.includedUsers
           .findIndex((element) => element.userName === 'test1') > -1).toBeTruthy()
    vm.$data.newIncludedUser = 'testUser'
    await vm.addIncludedUser()
    expect(vm.$data.editableDecision.includedUsers
      .findIndex((element) => element.userName === 'testUser') > -1).toBeFalsy()
  })

  it('removes a user from the includedUsers list', async () => {
    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test1'}, {userName: 'test2'}]
      }
    , localVue })
    const vm = wrapper.vm
    
    await vm.removeUser('test1')
    await vm.removeUser('testUser')
    expect(vm.$data.editableDecision.includedUsers).toStrictEqual([{userName: 'test2'}])
  })

  it('handles zero length prop inputs', async () => {
    const wrapper = shallowMount(EditDecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: []
      }
    , localVue })
    const vm = wrapper.vm
  })
})
