/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import DecisionCard from 'src/components/DecisionCard'
import axios from 'axios'
import auth from 'src/store/auth'
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
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

  beforeEach( () => {
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('calculates expiration time correctly', async () => {
    jest.useFakeTimers()
    Date.now = jest.fn(() => new Date('2020-09-01T09:26:00-04:00'))
    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
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
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-01T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm
    jest.advanceTimersByTime(1001)
    expect(vm.$data.expired).toBe(true)
  })

  it('uses a default users list when none is provided', async () => {
    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ownerUsername: 'test',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm
    expect(vm.$props.includedUsers).toStrictEqual([{ userName: 'test'}])
  })

  it('deletes a decision', async () => {
    axios.delete = jest.fn(() => Promise.resolve())
    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    await vm.onConfirmDelete()
    expect(axios.delete).toHaveBeenCalledTimes(1)
    expect(vm.$data.deleteDecisionDialog).toBe(false)
  })

  it('opens and closes the edit modal', async () => {
    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    await vm.openEditModal()
    expect(vm.editDecisionDialog).toBeTruthy()
    await vm.closeEditModal()
    expect(vm.editDecisionDialog).toBeFalsy()

  })

  it('catches axios errors', async () => {
    axios.delete= jest.fn(() => Promise.reject('Test Axios Error'))
    console.log = jest.fn()

    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: [{expirationDate: '2020-09-02T09:26:00-04:00'}],
        includedUsers: [{userName: 'test'}]
      }
    , localVue })
    const vm = wrapper.vm

    await vm.onConfirmDelete()
    
    expect(console.log).toHaveBeenCalledWith('Test Axios Error')
    expect(console.log).toHaveBeenCalledTimes(1)
  })

  it('handles zero length prop inputs', async () => {
    const wrapper = shallowMount(DecisionCard, {
      propsData: {
        id: 4,
        name: 'test',
        description: 'testDesc',
        ballots: []
      }
    , localVue })
    const vm = wrapper.vm
  })
})