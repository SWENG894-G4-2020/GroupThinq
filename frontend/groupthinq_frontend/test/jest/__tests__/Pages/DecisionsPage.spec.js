/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Decisions from 'src/pages/Decisions'
import DecisionCard from 'src/components/DecisionCard'
import axios from 'axios'
import auth from 'src/store/auth'
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

const testData = { data: { data: [
  {
    id: 4,
    name: "Title",
    description: "Description",
    expirationDate: '2020-09-01T09:26:00-04:00',
    ownerUsername: "test"
  }
]}}

const resetData = {
  name: '',
  // eslint-disable-next-line no-multi-str
  description: '',
  expirationDate: '',
  ownerUsername: 'test',
  users: []
}

describe('Decisions page tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  beforeEach( () => {
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('contains a Decision card after initial data fetch', async () => {
    // set up the Axios mock
    axios.get = jest.fn(() => Promise.resolve(testData))
    localVue.prototype.$axios = axios

    // mount the component under test
    const wrapper = shallowMount(Decisions, { localVue })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.isLoaded).toBe(true)
    expect(wrapper.findComponent(DecisionCard).exists()).toBe(true)
  })
  
  it('catches axios get errors', async () => {
    console.log = jest.fn()
    axios.get = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(Decisions, { localVue })
    const vm = wrapper.vm
    expect(vm.isError).toBe(false)
    await localVue.nextTick() // wait for the mounted function to finish
    //expect(console.log).toHaveBeenCalledWith('Test Error')
    expect(vm.isError).toBe(true)
  })

  it('opens and cancels the create decision dialog', async () => {
    const wrapper = shallowMount(Decisions, { localVue })
    const vm = wrapper.vm
    expect(vm.createDecisionDialog).toBe(false)
    await vm.createDecision()
    expect(vm.createDecisionDialog).toBe(true)
    await vm.closeCreateModal()
    expect(vm.createDecisionDialog).toBe(false)
  })

})
