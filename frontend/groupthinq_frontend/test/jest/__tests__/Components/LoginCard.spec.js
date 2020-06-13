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

  it('can cancel a login', async () => {
    const wrapper = shallowMount(LoginCard, { localVue, router})
    const vm = wrapper.vm

    await vm.cancel()
    await localVue.nextTick()

    expect(vm.$route.path).toBe('/')
  })
  
  it('can login when authenticated correctly', async () => {
    jest.clearAllMocks()
    axios.post= jest.fn(() => Promise.resolve({headers: {authorization: 'test'}}))
    auth.storeToken = jest.fn()

    const wrapper = shallowMount(LoginCard, { localVue, router})
    const vm = wrapper.vm

    await vm.login()
    await localVue.nextTick()

    expect(axios.post).toHaveBeenCalledTimes(1)
    expect(auth.storeToken).toHaveBeenCalledTimes(1)
    expect(vm.$route.path).toBe('/main')
  })
  
  it('catches axios errors', async () => {
    jest.clearAllMocks()
    axios.post= jest.fn(() => Promise.reject('Test Axios Error'))
    console.log = jest.fn()

    const wrapper = shallowMount(LoginCard, { localVue, router})
    const vm = wrapper.vm

    await vm.login()
    await localVue.nextTick()
    
    expect(console.log).toHaveBeenCalledWith('Test Axios Error')
    expect(auth.storeToken).toHaveBeenCalledTimes(0)
    expect(vm.$route.path).toBe('/login')
  })

  it('catches vue router errors', async () => {
    jest.clearAllMocks()
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

  /*

  it('passes the sanity check and creates a wrapper', () => {
    expect(wrapper.isVueInstance()).toBe(true)
  })

  it('has a created hook', () => {
    expect(typeof vm.increment).toBe('function')
  })

  it('accesses the shallowMount', () => {
    expect(vm.$el.textContent).toContain('rocket muffin')
    expect(wrapper.text()).toContain('rocket muffin') // easier
    expect(wrapper.find('p').text()).toContain('rocket muffin')
  })

  it('sets the correct default data', () => {
    expect(typeof vm.counter).toBe('number')
    const defaultData2 = QBUTTON.data()
    expect(defaultData2.counter).toBe(0)
  })

  it('correctly updates data when button is pressed', () => {
    const button = wrapper.find('button')
    button.trigger('click')
    expect(vm.counter).toBe(1)
  })

  it('formats a date without throwing exception', () => {
    // test will automatically fail if an exception is thrown
    // MMMM and MMM require that a language is 'installed' in Quasar
    let formattedString = date.formatDate(Date.now(), 'YYYY MMMM MMM DD')
    console.log('formattedString', formattedString)
  })
  */
