/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import SignUpCard from 'src/components/SignUpCard'
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

describe('SignUp Card tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.use(VueRouter)
  localVue.prototype.$axios = axios

  it('can cancel a signup', async () => {
    const wrapper = shallowMount(SignUpCard, { localVue, router})
    const vm = wrapper.vm
    await vm.cancel()
    expect(vm.$route.path).toBe('/')
  })

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
  }) 
})
