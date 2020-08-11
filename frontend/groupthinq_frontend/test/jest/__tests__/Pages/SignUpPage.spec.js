/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Signup from 'src/pages/Signup'
import SignUpCard from 'src/components/SignUpCard'
import VueRouter from 'vue-router'
import * as All from 'quasar'
// import langEn from 'quasar/lang/en-us' // change to any language you wish! => this breaks wallaby :(
const { Quasar, date } = All

// add all of the Quasar objects to the test harness
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Sign up page tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter
  
  localVue.use(VueRouter)
  localVue.use(Quasar, { components }) // , lang: langEn

  const wrapper = shallowMount(Signup, {
    localVue,
    router
  })
  const vm = wrapper.vm

  it('contains a Signup card', () => {
    expect(wrapper.findComponent(SignUpCard).exists())
      .toBe(true)
  })
})
