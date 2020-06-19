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
