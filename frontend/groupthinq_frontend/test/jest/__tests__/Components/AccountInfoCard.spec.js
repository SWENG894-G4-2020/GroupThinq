/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import AccountInfoCard from 'src/components/AccountInfoCard'
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

const testData = { 
  data: {
    birthDate: '',
    createdDate: '',
    emailAddress: '',
    firstName: '',
    lastLoggedIn: '',
    lastName: '',
    password: '',
    updatedDate: '',
    userName: ''
  }
}

describe('Account Info Card tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.use(VueRouter)
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

  beforeEach(() => jest.clearAllMocks())

  it('can cancel account editing', async () => {
    axios.get = jest.fn(() => Promise.resolve(testData))
    auth.getTokenData = jest.fn(() => ({sub: 'test'}))
    const wrapper = shallowMount(AccountInfoCard, { localVue, router})
    const vm = wrapper.vm
    await vm.onCancel()
    expect(vm.$data.deleteConfirm).toBe(false)
    expect(vm.$data.editEnabled).toBe(false)
  })

  it('can delete a user', async () => {
    axios.delete = jest.fn(() => Promise.resolve())
    const wrapper = shallowMount(AccountInfoCard, { localVue, router})
    const vm = wrapper.vm
    await vm.onDelete()
    expect(axios.delete).toHaveBeenCalledTimes(1)
    expect(vm.$route.path).toBe('/')
  })

  it('can edit a user information', async () => {
    axios.put = jest.fn(() => Promise.resolve())
    const wrapper = shallowMount(AccountInfoCard, { localVue, router})
    const vm = wrapper.vm
    await vm.onConfirm()
    expect(axios.put).toHaveBeenCalledTimes(1)
    expect(vm.$route.path).toBe('/main/account')
  })

  it('catches axios errors', async () => {
    axios.get= jest.fn(() => Promise.reject('Test Axios Error'))
    axios.put= jest.fn(() => Promise.reject('Test Axios Error'))
    axios.delete= jest.fn(() => Promise.reject('Test Axios Error'))
    console.log = jest.fn()

    const wrapper = shallowMount(AccountInfoCard, { localVue, router})
    const vm = wrapper.vm
    await vm.onConfirm()
    await vm.onDelete()
    await localVue.nextTick()
    
    expect(console.log).toHaveBeenCalledWith('Test Axios Error')
    expect(console.log).toHaveBeenCalledTimes(3)
  })


  /*
  it('can navigate a correctly formed signup', async () => {
    jest.clearAllMocks()
    axios.post= jest.fn(() => Promise.resolve({headers: {authorization: 'test'}}))
    auth.storeToken = jest.fn()

    const wrapper = shallowMount(SignUpCard, { localVue, router})
    const vm = wrapper.vm

    vm.$data.newUser.birthDate = new Date()
    await vm.signUp()
    await localVue.nextTick()

    expect(axios.post).toHaveBeenCalledTimes(2)
    expect(auth.storeToken).toHaveBeenCalledTimes(1)
    expect(vm.$route.path).toBe('/main')
  })
  
  it('catches axios errors', async () => {
    jest.clearAllMocks()
    axios.post= jest.fn(() => Promise.reject('Test Error'))
    console.log = jest.fn()

    const wrapper = shallowMount(SignUpCard, { localVue, router})
    const vm = wrapper.vm

    vm.$data.newUser.birthDate = new Date()
    await vm.signUp()
    await localVue.nextTick()
    
    expect(console.log).toHaveBeenCalledWith('Test Error')
    expect(auth.storeToken).toHaveBeenCalledTimes(0)
    expect(vm.$route.path).toBe('/login')
  }) */
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
