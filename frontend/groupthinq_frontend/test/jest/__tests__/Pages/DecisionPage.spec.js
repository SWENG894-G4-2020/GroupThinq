/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Decision from 'src/pages/Decision'
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
  }
]}}

const resetData = {
  name: '',
  description: '',
  ownerUsername: 'test',
  ballots: [{expirationDate: '2019-09-02T09:26:00-04:00'}],
  includedUsers: [{userName: 'test'}]
}

describe('Decision page tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  beforeEach( () => {
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser', role: 'Admin'}))
  })

  it('gets data correctly', async () => {
    // set up the Axios mock
    axios.get = jest.fn(() => Promise.resolve(testData))
    localVue.prototype.$axios = axios
    const $route = {params: {id: 4}}
    // mount the component under test
    const wrapper = shallowMount(Decision, { localVue, mocks: {$route} })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.isLoaded).toBe(true)
  })

  it('handles zero length data', async () => {
    console.log = jest.fn()
    // set up the Axios mock
    axios.get = jest.fn(() => Promise.resolve({data: {data: []}}))
    localVue.prototype.$axios = axios
    const $route = {params: {id: 4}}
    // mount the component under test
    const wrapper = shallowMount(Decision, { localVue, mocks: {$route} })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.isLoaded).toBe(false)
  })
  
  it('catches axios get errors', async () => {
    console.log = jest.fn()
    axios.get = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios
    const $route = {params: {id: 4}}
    // mount the component under test
    const wrapper = shallowMount(Decision, { localVue, mocks: {$route} })
    const vm = wrapper.vm

    expect(vm.isError).toBe(false)
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.isError).toBe(true)
  })

  it('sets expired', async () => {
    // mount the component under test
    const wrapper = shallowMount(Decision, { localVue })
    const vm = wrapper.vm

    expect(vm.expired).toBe(false)
    await vm.setExpired() // wait for the function call
    expect(vm.expired).toBe(true)
  })

  it('sets the mode to edit', async () => {
    // mount the component under test
    const wrapper = shallowMount(Decision, { localVue })
    const vm = wrapper.vm

    expect(vm.mode).toBe('view')
    await vm.onEdit() // wait for the function call
    expect(vm.mode).toBe('edit')
  })

  it('cancels an edit', async () => {
    // mount the component under test
    const wrapper = shallowMount(Decision, { localVue })
    const vm = wrapper.vm

    vm.mode = 'edit'
    vm.isLoaded = true
    await vm.onEditCancel() // wait for the function call
    expect(vm.mode).toBe('view')
    expect(vm.isLoaded).toBe(false)
  })

  it('deletes a decision', async () => {
    axios.delete = jest.fn(() => Promise.resolve())
    localVue.prototype.$axios = axios
    const wrapper = shallowMount(Decision, { localVue })
    const vm = wrapper.vm

    await vm.onConfirmDelete()
    expect(axios.delete).toHaveBeenCalledTimes(1)
    expect(vm.$data.deleteDecisionDialog).toBe(false)
  })

  it('builds a decision', async () => {

    const wrapper = shallowMount(Decision, { localVue })
    const vm = wrapper.vm
    vm.$refs = {'details': {getRequestObject: ()=>({ballots: []})}, 
                'ballot': {getRequestObject: ()=>('test')}}

    await vm.buildDecision()
    
  })

  it('confirms a valid edit', async () => {
    axios.put = jest.fn(() => Promise.resolve())
    localVue.prototype.$axios = axios
    const wrapper = shallowMount(Decision, { localVue })
    const vm = wrapper.vm
    vm.$refs = {'details': {isValid: ()=>(true), getRequestObject: ()=>({ballots: []})}, 
                'ballot': {isValid: ()=>(true), getRequestObject: ()=>('test')}}

    await vm.onEditConfirm()
    expect(vm.submissionValid).toBe(true)
  })

  it('rejects an invalid edit', async () => {
    axios.put = jest.fn(() => Promise.resolve())
    localVue.prototype.$axios = axios
    const wrapper = shallowMount(Decision, { localVue })
    const vm = wrapper.vm
    vm.$refs = {'details': {isValid: ()=>(false), getRequestObject: ()=>({ballots: []})}, 
                'ballot': {isValid: ()=>(true), getRequestObject: ()=>('test')}}

    await vm.onEditConfirm()
    expect(vm.submissionValid).toBe(false)
  })

  it('catches axios put errors', async () => {
    console.log = jest.fn()
    axios.put = jest.fn(() => Promise.reject())
    localVue.prototype.$axios = axios
    const wrapper = shallowMount(Decision, { localVue })
    const vm = wrapper.vm
    vm.$refs = {'details': {isValid: ()=>(true), getRequestObject: ()=>({ballots: []})}, 
                'ballot': {isValid: ()=>(true), getRequestObject: ()=>('test')}}

    await vm.onEditConfirm()
    expect(vm.isError).toBe(true)
  })
})
