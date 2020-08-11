/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import LoginCard from 'src/components/LoginCard'
import axios from 'axios';
import VueRouter from 'vue-router'
import * as All from 'quasar'
import auth from 'src/store/auth';

// add all of the Quasar objects to the test harness
const { Quasar, date, ClosePopup } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Login Card tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.use(VueRouter)
  localVue.prototype.$axios = axios

  beforeEach( () => {
    jest.clearAllMocks()
    auth.removeTokens = jest.fn()
  })

  it('can cancel a login', async () => {
    const wrapper = shallowMount(LoginCard, { localVue, router})
    const vm = wrapper.vm

    await vm.cancel()
    await localVue.nextTick()

    expect(vm.$route.path).toBe('/')
  })
  
  it('can login when authenticated correctly', async () => {
    axios.post= jest.fn(() => Promise.resolve({headers: {authorization: 'test'}}))
    auth.storeToken = jest.fn()

    const wrapper = shallowMount(LoginCard, { localVue, router})
    const vm = wrapper.vm

    await vm.login()
    await localVue.nextTick()

    expect(axios.post).toHaveBeenCalledTimes(1)
    expect(auth.storeToken).toHaveBeenCalledTimes(1)
    expect(vm.$data.isError).toBe(false)
    expect(wrapper.find("#LoginError").exists()).toBe(false)
    expect(vm.$route.path).toBe('/main')
  })
  
  it('catches axios errors', async () => {
    axios.post= jest.fn(() => Promise.reject('Test Axios Error'))
    console.log = jest.fn()

    const wrapper = shallowMount(LoginCard, { localVue, router})
    const vm = wrapper.vm

    await vm.login()
    await localVue.nextTick()
    
    expect(console.log).toHaveBeenCalledWith('Test Axios Error')
    expect(auth.storeToken).toHaveBeenCalledTimes(0)
    expect(vm.$data.isError).toBe(true)
    expect(wrapper.find("#LoginError").exists()).toBe(true)
  })

  it('catches vue router errors', async () => {
    axios.post= jest.fn(() => Promise.resolve({headers: {authorization: 'test'}}))
    auth.storeToken = jest.fn()
    router.push = jest.fn(() => Promise.reject('Test Router Error'))
    console.log = jest.fn()

    const wrapper = shallowMount(LoginCard, { localVue, router})
    const vm = wrapper.vm

    await vm.login()
    await localVue.nextTick()
    
    expect(console.log).toHaveBeenCalledWith('Vue router error: Test Router Error')
  })
})
