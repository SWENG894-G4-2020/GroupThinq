/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Login from 'src/pages/Login'
import LoginCard from 'src/components/LoginCard'
import VueRouter from 'vue-router'
import * as All from 'quasar'
// import langEn from 'quasar/lang/en-us' // change to any language you wish! => this breaks wallaby :(
const { Quasar, date } = All

const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Welcome Layout tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter
  
  localVue.use(VueRouter)
  localVue.use(Quasar, { components }) // , lang: langEn

  const wrapper = shallowMount(Login, {
    localVue,
    router
  })
  const vm = wrapper.vm

  it('contains an Account Info card', () => {
    expect(wrapper.findComponent(LoginCard).exists())
      .toBe(true)
  })
})
