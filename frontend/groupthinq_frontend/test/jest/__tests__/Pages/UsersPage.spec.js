/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Users from 'src/pages/Users'
import UserCard from 'src/components/UserCard'
import axios from 'axios';
import * as All from 'quasar'

jest.mock('axios')

// add all of the Quasar objects to the test harness
const { Quasar, date } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Users page tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  it('contains a User card after initial data fetch', async () => {
    const testData = { data: [
      {
        userName: "jDoe",
        firstName: "John",
        lastName: "Doe",
        emailAddress: "jDoe@foo.com"
      }
    ]}
    axios.get.mockImplementation(() => Promise.resolve(testData))
    localVue.prototype.$axios = axios
    const wrapper = shallowMount(Users, { localVue })
    const vm = wrapper.vm
    await localVue.nextTick() // wait for the mounted function to finish
    expect(vm.isLoaded).toBe(true)
    expect(wrapper.findComponent(UserCard).exists()).toBe(true)
  })
  
  it('catches axios errors', async () => {
    console.log = jest.fn()
    axios.get.mockImplementation(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios
    const wrapper = shallowMount(Users, { localVue })
    const vm = wrapper.vm
    await localVue.nextTick() // wait for the mounted function to finish
    expect(console.log).toHaveBeenCalledWith('Test Error')
  })
})
