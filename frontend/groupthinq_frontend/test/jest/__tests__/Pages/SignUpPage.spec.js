/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Signup from 'src/pages/Signup'
import SignupCard from 'src/components/SignupCard'
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
    expect(wrapper.findComponent(SignupCard).exists())
      .toBe(true)
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
})