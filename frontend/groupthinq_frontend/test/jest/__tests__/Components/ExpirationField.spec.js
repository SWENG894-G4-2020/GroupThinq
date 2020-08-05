/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import ExpirationField from 'src/components/ExpirationField'
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

describe('Expiration field tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

  const testPropsTemplate = {
    initialDate: "2020-10-10T04:00:00.000Z",
    showTimer: true,
    mode: 'view'
  }
  var testProps = {}

  beforeEach( () => {
    testProps = JSON.parse(JSON.stringify(testPropsTemplate))
    jest.clearAllMocks()
  })

  it('calculates expiration time correctly', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-01T09:26:00-04:00'))
    const wrapper = shallowMount(ExpirationField , { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.calculateRemainingTime()
    jest.advanceTimersByTime(1001)
    expect(Date.now).toHaveBeenCalled()
  })

  it('exit expiration calculation when expired', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-11-11T09:26:00-04:00'))
    const wrapper = shallowMount(ExpirationField, { propsData: testProps, localVue })
    const vm = wrapper.vm
    await vm.calculateRemainingTime()
    jest.advanceTimersByTime(1001)
    expect(Date.now).toHaveBeenCalled()
  })

  it('determines the status for an expired vote', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-02T09:26:00-04:00'))
    const wrapper = shallowMount(ExpirationField, { propsData: testProps, localVue })
    const vm = wrapper.vm
    expect(ExpirationField.computed.expired
      .call({datetime: "2020-10-10T04:00:00.000Z"}))
      .toBe(false)
  })

  it('opens and closes the edit modal', async () => {
    const wrapper = shallowMount(ExpirationField, { propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.openDatetimeDialog()
    expect(vm.pickDatetimeDialog).toBeTruthy()
    await vm.closeDatetimeDialog()
    expect(vm.pickDatetimeDialog).toBeFalsy()
  })

  it('will not validate expired dates on create', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-02T09:26:00-04:00'))
    const wrapper = shallowMount(ExpirationField, { 
      propsData: {
        initialDate: "2018-10-10T04:00:00.000Z",
        showTimer: true,
        mode: 'create'
      }, 
    localVue })
    const vm = wrapper.vm
    expect(vm.isValid.call()).toBeFalsy()
    
  })

  it('will not validate malformed dates', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-02T09:26:00-04:00'))
    const wrapper = shallowMount(ExpirationField, { 
      propsData: {
        initialDate: "la la la la",
        showTimer: true,
        mode: 'create'
      }, 
    localVue })
    const vm = wrapper.vm
    expect(vm.isValid.call({datetime: "la la la la"})).toBeFalsy()
  })

  it('recalculates on a decision change', () => {
    date.formatDate = jest.fn(() => '2020/10/12 00:00')
    const wrapper = shallowMount(ExpirationField, { propsData: testProps, localVue })
    const vm = wrapper.vm

    testProps.initialDate = "2020-10-12T04:00:00.000Z"
    expect(date.formatDate).toHaveBeenCalled()
  })

})