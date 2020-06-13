/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import UserMenu from 'src/components/UserMenu'
import auth from 'src/store/auth'
import VueRouter from 'vue-router'
import * as All from 'quasar'

// add all of the Quasar objects to the test harness
const { Quasar, date } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('User menu tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.use(VueRouter)
  localVue.directive('close-popup', All.ClosePopup)

  it('contains a username after being mounted', async () => {
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
    // mount the component under test
    const wrapper = shallowMount(UserMenu, { localVue })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.userName).toBe('testUser')
  })

  it('can navigate to account settings', async () => {
    const wrapper = shallowMount(UserMenu, { localVue, router})
    const vm = wrapper.vm
    await vm.goToSettings()
    expect(vm.$route.path).toBe('/main/account')
  })

  it('can logout by deleting localstorage', async () => {
    auth.removeTokens = jest.fn()
    const wrapper = shallowMount(UserMenu, { localVue, router })
    const vm = wrapper.vm
    await vm.logout()
    expect(auth.removeTokens).toBeCalledTimes(1)
    expect(vm.$route.path).toBe('/')
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
