/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import UserCard from 'src/components/UserCard'
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

const testData = {data: { 
  data: [{
    emailAddress: '',
    firstName: '',
    lastName: '',
    userName: ''
  }]}
}

describe('User Card tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.use(VueRouter)
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

  beforeEach(() => jest.clearAllMocks())

  it('determine the correct user role', async () => {
    auth.getTokenData = jest.fn(() => ({sub: 'test', role: 'Admin'}))
    const wrapper = shallowMount(UserCard, { localVue, router})
    const vm = wrapper.vm
    await vm.$nextTick()
    expect(vm.$data.currentUserRole).toBe('Admin')
  })

})
