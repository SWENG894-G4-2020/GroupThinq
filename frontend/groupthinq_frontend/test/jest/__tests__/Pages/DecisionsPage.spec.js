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

const testData = { data: [
  {
    title: "Title",
    description: "Description",
    expiration: new Date(),
    owner: "test",
    users: []
  }
]}

const resetData = {
  title: '',
  // eslint-disable-next-line no-multi-str
  description: 'Decision description. Dolorem numquam nor nesciunt and adipisci, explicabo ratione. \
                Architecto aut. Ab esse ad. Esse. Accusantium quo. Id rem dolorem nor \
                sequi dolor yet adipisci. Quam do et anim dolorem. Dolore officia labore \
                anim pariatur architecto, cillum. Aut anim so do. Vel. Si nostrud quia, \
                for iure so lorem. Veritatis nostrum yet nisi yet ullamco.',
  expiration: '',
  users: []
}

describe('Decisions page tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

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
    await vm.onCancel()
    expect(vm.createDecisionDialog).toBe(false)
  })

  it('opens and cancels the create decision dialog', async () => {
    auth.getTokenData = jest.fn(() => ({sub: 'testOwner'}))

    const wrapper = shallowMount(Decisions, { localVue })
    const vm = wrapper.vm
    await localVue.nextTick()
    expect(vm.decisionList.length).toBe(1)
    vm.$data.newDecision = testData.data
    await vm.onCreate()
    expect(vm.decisionList.length).toBe(2)
  })

})
