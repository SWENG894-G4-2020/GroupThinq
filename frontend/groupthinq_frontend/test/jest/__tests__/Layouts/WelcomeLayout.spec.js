/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import WelcomeLayout from 'src/layouts/WelcomeLayout'
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

  const wrapper = mount(WelcomeLayout, {
    localVue,
    router
  })
  const vm = wrapper.vm

  it('does not contain any active data', () => {
    expect(WelcomeLayout.data()).toEqual({})
  })
})
