/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import NewDecision from 'src/pages/NewDecision'
import auth from 'src/store/auth'
import axios from 'axios'
import VueRouter from 'vue-router'
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


describe('Decision page tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.use(VueRouter)

  beforeEach( () => {
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser', role: 'Admin'}))
  })

  it('builds a decision', async () => {

    const wrapper = shallowMount(NewDecision, { localVue })
    const vm = wrapper.vm
    vm.$refs = {'details': {getRequestObject: ()=>({ballots: []})}, 
                'ballot': {getRequestObject: ()=>('test')}}

    await vm.buildDecision()
    
  })

  it('confirms a valid create', async () => {
    axios.post = jest.fn(() => Promise.resolve())
    localVue.prototype.$axios = axios
    const wrapper = shallowMount(NewDecision, { localVue, router })
    const vm = wrapper.vm
    vm.$refs = {'details': {isValid: ()=>(true), getRequestObject: ()=>({ballots: []})}, 
                'ballot': {isValid: ()=>(true), getRequestObject: ()=>('test')}}

    await vm.onCreate()
    expect(vm.submissionValid).toBe(true)
  })

  it('rejects an invalid create', async () => {
    const wrapper = shallowMount(NewDecision, { localVue, router })
    const vm = wrapper.vm
    vm.$refs = {'details': {isValid: ()=>(false), getRequestObject: ()=>({ballots: []})}, 
                'ballot': {isValid: ()=>(true), getRequestObject: ()=>('test')}}

    await vm.onCreate()
    expect(vm.submissionValid).toBe(false)
  })

  it('handles axios post errors', async () => {
    console.log = jest.fn()
    axios.post = jest.fn(() => Promise.reject())
    localVue.prototype.$axios = axios
    const wrapper = shallowMount(NewDecision, { localVue, router })
    const vm = wrapper.vm
    vm.$refs = {'details': {isValid: ()=>(true), getRequestObject: ()=>({ballots: []})}, 
                'ballot': {isValid: ()=>(true), getRequestObject: ()=>('test')}}

    await vm.onCreate()
    expect(vm.isError).toBe(true)
  })

})
