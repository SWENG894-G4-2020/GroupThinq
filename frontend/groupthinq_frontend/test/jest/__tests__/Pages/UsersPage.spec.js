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
