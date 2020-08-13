import { mount, createLocalVue, shallowMount } from '@vue/test-utils'
import Help from 'src/pages/Help'
import FaqAccordionCard from 'src/components/FaqAccordionCard'
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

describe('Faq Accordion Card tests', () => {
    const localVue = createLocalVue()
    localVue.use(Quasar, { components }) // , lang: langEn
    localVue.directive('close-popup', All.ClosePopup)
    localVue.prototype.$axios = axios
    var testProps = []
    const testPropItems = [
          {
            title: 'What is groupThinq?',
            value: 'groupThinq is a decision making engine for any group of people to use to facilitate a vote',
            category: 'General Questions'
          },
          {
            title: 'How can I update my password?',
            value: 'Currently you are unable to update your password, so make sure to keep it somewhere safe.',
            category: 'General Questions'
          },
          {
            title: 'What are the different kind of things I can do in the groupThinq application?',
            value: "While the function of this application is focused on setting up a vote for something. The boundaries of what you can do is only limited by imagination. Where do you want to go to lunch? What should your D&D campaign be about? Which Jonas brother really is the best? Any question you've ever had can be put up to a vote",
            category: 'Account Questions'
          }
    ]
    testProps = JSON.parse(JSON.stringify(testPropItems))
    const wrapperHelp = shallowMount(Help)
    const wrapper = shallowMount(FaqAccordionCard, {
        propsData: {
          items: [{questionProperty: "title", answerProperty: "value", tabName: "category"}],
          questionProperty: "title",
          answerProperty: "value",
          tabName: "category",
          activeColor: "#1976D2",
          initialQuestionIndex: 0
        }
      })
    const vm = wrapper.vm


    it("renders faq section style", () => {
      expect(vm.activeColor).toBe("#1976D2")
    })

    it("renders faq section generated questions", () => {
      vm.generateQuestionClasses(0)
      expect(vm.generateQuestionClasses()).toStrictEqual(["accordion__title", null])

    })

    it("renders faq section generated questions index", () => {
      wrapper.setProps(testProps)
      vm.makeActive(1)
      expect(vm.$data.activeQuestionIndex).toBe(1)
    })

    it("renders faq section generated questions index nullable path", () => {
      wrapper.setProps(testProps)
      vm.makeActive(1)
      expect(vm.$data.activeQuestionIndex).toBe(null)// if we run makeActive on the active question, we basically minimize the question tab

    })

    it("renders faq section generated buttons", () => {
      wrapper.setProps(testProps)
      expect(vm.generateButtonClasses()).toContain("accordion__toggle-button")

    })

    it("renders faq section generated buttons nullable path", () => {
      wrapper.setProps(testProps)
      vm.makeActive(0)
      expect(vm.generateButtonClasses(0)).toContain("accordion__toggle-button")
    })

    it("renders faq section generated categories", () => {
      wrapper.setProps(testProps)
      vm.makeActiveCategory("Account Questions", 1)
      expect(vm.$data.activeTab).toBe("Account Questions")
      expect(vm.generateCategoryClasses("Account Questions")).toContain("faq__nav-item")
    })

    it("renders faq section generated categories nullable path", () => {
      wrapper.setProps(testProps)
      vm.makeActiveCategory("Account Questions", 1)
      vm.makeActiveCategory("Account Questions", 1)
      expect(vm.$data.activeTab).toBe("Account Questions")
      expect(vm.generateCategoryClasses()).toContain("faq__nav-item")
    })
})
