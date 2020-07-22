/* eslint-disable */
/**
 * @jest-environment jsdom
 */

import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import VotingCard from 'src/components/VotingCard'
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

describe('Voting Card tests', () => {
  const localVue = createLocalVue()
  localVue.use(Quasar, { components }) // , lang: langEn

  const testPropsTemplate = {
    previousVote: {title: "Ballot Option 2"},
    ballotId: 10,
    title: "Decision 1",
    description: "Decision with Ballot and 2 Ballot Options",
    ballotOptions: [
      {
        title: "Ballot Option 1",
        userName: "testuser1"
      },
      {
          title: "Ballot Option 2",
          userName: "testuser1"
      }]
  }
  var testProps = {}

  beforeEach( () => {
    testProps = JSON.parse(JSON.stringify(testPropsTemplate))
    jest.clearAllMocks()
    auth.getTokenData = jest.fn(() => ({sub: 'testUser'}))
  })

  it('catches submission and axios post errors', async () => {
    console.log = jest.fn()
    axios.post = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(VotingCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    vm.$data.voteResult = 'Ballot Option 1'
    await vm.onSubmit()
    expect(console.log).toHaveBeenCalledWith('Test Error')
    expect(console.log).toHaveBeenCalledTimes(1)

    vm.$data.voteResult = 'Ballot Option 3'
    await vm.onSubmit()

  })

  it('posts a new vote correctly', async () => {
    console.log = jest.fn()
    axios.post = jest.fn(() => Promise.resolve())
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(VotingCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    vm.$data.voteResult = 'Ballot Option 1'
    await vm.onSubmit()
    expect(axios.post).toHaveBeenCalledTimes(1)
  })

  it('correctly formats ballot options', async () => {
    expect(VotingCard.computed.formattedVoteOptions
      .call({ballotOptions: testProps.ballotOptions}))
      .toStrictEqual(
      [{"label": "Ballot Option 1", "value": "Ballot Option 1"}, 
      {"label": "Ballot Option 2", "value": "Ballot Option 2"}])

  })

  /*
  it('catches axios put errors', async () => {
    console.log = jest.fn()
    axios.put = jest.fn(() => Promise.reject("Test Error"))
    localVue.prototype.$axios = axios

    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.onEditConfirm() // wait for the mounted function to finish
    expect(console.log).toHaveBeenCalledWith('Test Error')
  })

  it('gets all users', async () => {
    axios.get = jest.fn(() => Promise.resolve({data: { data: [{ userName: 'test1' }, { userName: 'test2' }]}}))
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm
    
    await localVue.nextTick()
    expect(axios.get).toHaveBeenCalledTimes(1)
    expect(vm.$data.allUsersList).toStrictEqual(['test1', 'test2'])
  })

  it('edits a decision', async () => {
    axios.put = jest.fn()
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.onEditConfirm()
    expect(axios.put).toHaveBeenCalledTimes(1)
  })

  it('emits a cancel event on cancel', async () => {
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.onCancel()
    expect(wrapper.emitted().editClose).toBeTruthy()
  })

  it('filters user hints', async () => {
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    vm.$data.allUsersList = ['test1', 'test2']
    await vm.filterFn('test', (fn) => (fn()), () => {})
    await vm.filterFn('t', (fn) => {fn()}, () => {})
  })

  it('can reset the edited decision data', async () => {
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    await vm.fillEditableDecision()
  })

  it('can add a user to the addedUsers list', async () => {
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm

    vm.$data.newIncludedUser = 'test1'
    await vm.addIncludedUser()
    expect(vm.$data.editableDecision.includedUsers
           .findIndex((element) => element.userName === 'test1') > -1).toBeTruthy()
    vm.$data.newIncludedUser = 'testUser'
    await vm.addIncludedUser()
    expect(vm.$data.editableDecision.includedUsers
      .findIndex((element) => element.userName === 'testUser') > -1).toBeFalsy()
  })

  it('removes a user from the includedUsers list', async () => {
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm
    
    await vm.removeUser('test')
    await vm.removeUser('testUser')
    expect(vm.$data.editableDecision.includedUsers).toStrictEqual([{userName: 'test2'}])
  })

  it('handles zero length prop inputs', async () => {
    testProps.includedUsers = undefined
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm
  })

  it('handles invalid submission information', async () => {
    const wrapper = shallowMount(EditDecisionCard, {propsData: testProps, localVue })
    const vm = wrapper.vm
    vm.$data.newExpirationDate = '20'
    await vm.checkValidSubmit()
    vm.$data.editableDecision.description = ''
    await vm.checkValidSubmit()
    vm.$data.editableDecision.name = ''
    await vm.checkValidSubmit()
    await vm.onEditConfirm()
  })

  */
})
