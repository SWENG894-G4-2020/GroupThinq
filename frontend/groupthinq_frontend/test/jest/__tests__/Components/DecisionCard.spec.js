/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import DecisionCard from 'src/components/DecisionCard'
import * as All from 'quasar'

// add all of the Quasar objects to the test harness
const { Quasar, date, ClosePopup } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Decision Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  it('calculates expiration time correctly', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-01T09:26:00-04:00'))
    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        title: 'test',
        expiration: new Date('2020-09-02T09:26:00-04:00'),
        users: []
      }
    , localVue })
    const vm = wrapper.vm
    await vm.calculateRemainingTime()
    jest.advanceTimersByTime(1001)
    expect(Date.now).toHaveBeenCalled()
  })

  it('determines the status for an expired vote', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-02T09:26:00-04:00'))
    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        title: 'test',
        expiration: new Date('2020-09-01T09:26:00-04:00'),
        users: []
      }
    , localVue })
    const vm = wrapper.vm
    jest.advanceTimersByTime(1001)
    expect(vm.$data.expired).toBe(true)
  })

  it('uses a default users list when none is provided', async () => {
    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        title: 'test',
        expiration: new Date('2020-09-01T09:26:00-04:00'),
      }
    , localVue })
    const vm = wrapper.vm
    expect(vm.$props.users).toStrictEqual([])
  })
})