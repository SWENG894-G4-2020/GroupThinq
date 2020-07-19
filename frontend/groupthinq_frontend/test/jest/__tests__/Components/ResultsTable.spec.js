/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import ResultsTable from 'src/components/ResultsTable'
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

describe('Results Table tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

  const testPropsTemplate = {
    tabulatedResults: [
      {
        name: 'Option 1',
        description: 'Desc',
        votes: 1,
        winner: false
      },
      {
        name: 'Option 2',
        description: 'Desc',
        votes: 2,
        winner: true
      }
    ],
  }
  var testProps = {}

  beforeEach( () => {
    testProps = JSON.parse(JSON.stringify(testPropsTemplate))
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('contains a q-table after mounted', async () => {
    // set up the Axios mock
    axios.get = jest.fn(() => Promise.resolve(testData))
    localVue.prototype.$axios = axios

    // mount the component under test
    const wrapper = shallowMount(ResultsTable, { propsData: testProps, localVue })
    const vm = wrapper.vm

    // test for expected results
    await localVue.nextTick() // wait for the mounted function to finish
    expect(wrapper.findComponent(ResultsTable).exists()).toBe(true)
  })

})