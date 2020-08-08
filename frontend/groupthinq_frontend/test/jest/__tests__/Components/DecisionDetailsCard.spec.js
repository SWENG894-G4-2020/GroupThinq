/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import DecisionDetailsCard from 'src/components/DecisionDetailsCard'
import axios from 'axios'
import auth from 'src/store/auth'
import * as All from 'quasar'
import { exp, valueAndGrad } from '@tensorflow/tfjs'

// add all of the Quasar objects to the test harness
const { Quasar, date, ClosePopup } = All
const components = Object.keys(All).reduce((object, key) => {
  const val = All[key]
  if (val && val.component && val.component.name != null) {
    object[key] = val
  }
  return object
}, {})

describe('Ballot Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn
  localVue.directive('close-popup', All.ClosePopup)
  localVue.prototype.$axios = axios

  const testPropsTemplate = {
    mode: 'view',
    decision: {
      id: 5,
      name: "Decision 1",
      description: "Decision with Ballot and 4 Ballot Options",
      ownerUsername: "testUser",
      includedUsers: [
          {userName: "testUser"},
          {userName: "testUser1"},
          {userName: "testUser2"}
      ],
      ballots: [
        {
        ballotOptions: [
          {
            ballotId: 0,
            createdDate: "2020-07-19T02:02:31.006Z",
            description: "string",
            id: 0,
            title: "option 1",
            updatedDate: "2020-07-19T02:02:31.006Z",
            userName: "string"
          },
          {
            ballotId: 0,
            createdDate: "2020-07-19T02:02:31.006Z",
            description: "string",
            id: 1,
            title: "option 2",
            updatedDate: "2020-07-19T02:02:31.006Z",
            userName: "string"
          },
          {
            ballotId: 0,
            createdDate: "2020-07-19T02:02:31.006Z",
            description: "string",
            id: 2,
            title: "option 3",
            updatedDate: "2020-07-19T02:02:31.006Z",
            userName: "string"
          }
        ],
        ballotTypeId: 1,
        createdDate: "2020-07-19T02:02:31.006Z",
        decisionId: 0,
        expirationDate: "2020-07-19T02:02:31.006Z",
        id: 0,
        updatedDate: "2020-07-19T02:02:31.006Z"
      }
      ]
    }
  }
  var testProps = {}

  const testUsersResponse = { 
    data: { 
      data: [
    {userName: "testUser"},
    {userName: "testUser1"},
    {userName: "testUser2"}
      ]
    }
  }

  beforeEach( () => {
    testProps = JSON.parse(JSON.stringify(testPropsTemplate))
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('catches axios errors when retrievin users', async () => {
    axios.get= jest.fn(() => Promise.reject('Test Axios Error'))
    console.log = jest.fn()
    const wrapper = shallowMount(DecisionDetailsCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.getAllUsers()
    
    expect(console.log).toHaveBeenCalledWith('Test Axios Error')
    expect(console.log).toHaveBeenCalledTimes(2)
  })

  it('gets all users and populates them when a decision is provided', async () => {
    axios.get= jest.fn(() => Promise.resolve(testUsersResponse))
    const wrapper = shallowMount(DecisionDetailsCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.$nextTick()
    
    expect(vm.$data.allUsersList).toHaveLength(3)
    expect(vm.$data.selectedUsers).toHaveLength(3)
  })

  it('validates the fields', async () => {
    const wrapper = shallowMount(DecisionDetailsCard, { propsData: { mode: 'create' }, localVue })
    const vm = wrapper.vm

    vm.$data.name = ''
    expect(vm.isValid.call()).toBe(false)
    vm.$data.name = 'jdkl;ajflk;dsajfl;skdajfl;kasdjkjlkjlk;j;lklkjl;khlk;hlk;sdahlk;hlk;hlk;hlk;hlk;hl;khklhkl;hkl;hkl;hkl;h;klh;klhkl;hlk;hlkhl;khl;khl;khlkhlkhkgfaafdafdasfdasfadfadsfsdafsadf'
    expect(vm.isValid.call()).toBe(false)
    vm.$data.name = 'good'
    expect(vm.isValid.call()).toBe(true)
  })

  it('creates a request object with an initial decision', async () => {
    const wrapper = shallowMount(DecisionDetailsCard, { propsData: testProps, localVue })
    const vm = wrapper.vm
    const reqObj = vm.getRequestObject()
    expect(reqObj.includedUsers).toHaveLength(3)
    expect(reqObj.name).toEqual('Decision 1')
    expect(reqObj.description).toEqual('Decision with Ballot and 4 Ballot Options')
    expect(reqObj.ownerUsername).toEqual('testUser')
    expect(reqObj.id).toEqual(5)

    wrapper.setProps({ mode: 'create', decision: {} })

    const reqObj2 = vm.getRequestObject()
  })

  it('removes current user when switching to edit mode', async () => {
    axios.get= jest.fn(() => Promise.resolve(testUsersResponse))
    const wrapper = shallowMount(DecisionDetailsCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.$nextTick()
    const modProps = JSON.parse(JSON.stringify(testPropsTemplate))
    modProps.mode = 'edit'
    wrapper.setProps( modProps )
    await vm.$nextTick()
    expect(vm.$data.selectedUsers).toHaveLength(2)
  })

  it('filters the users in the search function', async () => {
    const wrapper = shallowMount(DecisionDetailsCard, { propsData: { mode: 'create' }, localVue })
    const vm = wrapper.vm

    vm.$data.allUsersList = ['test1', 'test2']
    await vm.filterFn('test', (fn) => (fn()), () => {})
    await vm.filterFn('t', (fn) => {fn()}, () => {})
  })

  it('populates the decisin when provided and initial decision', async () => {
    testProps.decision.description = ''
    axios.get= jest.fn(() => Promise.resolve(testUsersResponse))
    const wrapper = shallowMount(DecisionDetailsCard, { propsData: testProps, localVue })
    const vm = wrapper.vm

    vm.resetForm()
    vm.populateWithDecision()
    expect(vm.$data.selectedUsers).toHaveLength(3)
    expect(vm.$data.name).toEqual('Decision 1')
    expect(vm.$data.description).toEqual('')
  })

})
