/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import AccountInfo from 'src/pages/AccountInfo'
import AccountInfoCard from 'src/components/AccountInfoCard'
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

describe('Account Info Page tests', () => {
  const localVue = createLocalVue()
  const router = new VueRouter
  
  localVue.use(VueRouter)
  localVue.use(Quasar, { components }) // , lang: langEn

  it('contains an Account Info card', async () => {
    const wrapper = shallowMount(AccountInfo, {
      localVue,
      router
    })
    const vm = wrapper.vm

    await localVue.nextTick()
    expect(wrapper.findComponent(AccountInfoCard).exists())
      .toBe(true)
  })
})
