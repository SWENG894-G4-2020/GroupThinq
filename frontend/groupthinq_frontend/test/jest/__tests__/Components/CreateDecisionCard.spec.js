/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import CreateDecisionCard from 'src/components/CreateDecisionCard'
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

describe('Create Decision Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  beforeEach( () => {
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
    auth.removeTokens = jest.fn()
  })

  it('catches axios get errors', async () => {
    console.log = jest.fn()
    axios.get = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm

    await localVue.nextTick()// wait for the mounted function to finish
    expect(console.log).toHaveBeenCalledWith('Test Error')

  })

  it('catches axios post errors', async () => {
    console.log = jest.fn()
    axios.post = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm

    vm.$data.newDecision.name = 'testTitle'
    vm.$data.newDecision.description = 'testDescription'
    vm.$data.newExpirationDate = '2020-09-01T09:26:00-04:00'
    vm.$data.optionsList = [
      {
        title: "Ballot Option 1",
        userName: "testuser1"
      }]
    await vm.onCreate() // wait for the mounted function to finish
    expect(console.log).toHaveBeenCalledWith('Test Error')
  })

  it('gets all users', async () => {
    axios.get = jest.fn(() => Promise.resolve({data: { data: [{ userName: 'test1' }, { userName: 'test2' }]}}))
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm
    
    await localVue.nextTick()
    expect(axios.get).toHaveBeenCalledTimes(1)
    expect(vm.$data.allUsersList).toStrictEqual(['test1', 'test2'])
  })

  it('creates a decision', async () => {
    axios.post = jest.fn()
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm
    
    vm.$data.newDecision.name = "testTitle"
    vm.$data.newDecision.description = "testDescription"
    vm.$data.newExpirationDate="2020-09-01T09:26:00-04:00"
    vm.$data.optionsList = [
      {
        title: "Ballot Option 1",
        userName: "testuser1"
      }]
    vm.$data.addedUsers = ['test1', 'test2']
    await vm.onCreate()
    expect(axios.post).toHaveBeenCalledTimes(1)
  })

  it('emits a cancel event on cancel', async () => {
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm

    await vm.onCancel()
    expect(wrapper.emitted().createClose).toBeTruthy()
  })

  it('filters user hints', async () => {
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm

    vm.$data.allUsersList = ['test1', 'test2']
    await vm.filterFn('test', (fn) => (fn()), () => {})
    await vm.filterFn('t', (fn) => {fn()}, () => {})
  })

  it('can reset the new decision data', async () => {
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm

    await vm.resetNewDecision()
  })

  it('can add a user to the addedUsers list', async () => {
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm

    vm.$data.newIncludedUser = 'test'
    await vm.addIncludedUser()
    expect(vm.$data.addedUsers.includes('test')).toBeTruthy()
    vm.$data.newIncludedUser = 'testUser'
    await vm.addIncludedUser()
    expect(vm.$data.addedUsers.includes('testUser')).toBeFalsy()
  })

  it('removes a user from the includedUsers list', async () => {
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm
    
    vm.$data.addedUsers = ['test1', 'test2']
    await vm.removeUser('test1')
    expect(vm.$data.addedUsers).toStrictEqual(['test2'])
  })

  it('adds a decision option', async () => {
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm
    
    vm.$data.newOption.title = "testTitle"
    await vm.addDecisionOption()
    expect(vm.$data.optionsList.length).toBe(1)

    await vm.addDecisionOption()
    expect(vm.$data.optionsList.length).toBe(1)
  })

  it('removes a decision option', async () => {
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm
    
    vm.$data.optionsList = [{title: 'testTitle'}]
    await vm.removeDecisionOption({title: 'testTitle'})
    expect(vm.$data.optionsList.length).toBe(0)
  })

  it('handles invalid submission information', async () => {
    const wrapper = shallowMount(CreateDecisionCard, { localVue })
    const vm = wrapper.vm
    vm.$data.newDecision.name = 'testTitle'
    vm.$data.newDecision.description = 'testDescription'
    vm.$data.newExpirationDate = '2020/10/10'
    vm.$data.optionsList = [
      {
        title: "Ballot Option 1",
        userName: "testuser1"
      }]
    await vm.checkValidSubmit()
    vm.$data.optionsList = []
    await vm.checkValidSubmit()
    vm.$data.newExpirationDate = '20'
    await vm.checkValidSubmit()
    vm.$data.newDecision.name = ''
    await vm.checkValidSubmit()
    await vm.onCreate()
  })
})
