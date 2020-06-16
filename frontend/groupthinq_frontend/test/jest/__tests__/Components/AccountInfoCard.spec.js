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
})
