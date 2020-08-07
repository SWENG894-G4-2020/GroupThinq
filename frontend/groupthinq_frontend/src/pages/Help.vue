<template>
 <div class="flex flex-center">
    <h3>Frequently Asked Questions</h3>
    <!-- DEFAULT -->
    <VueFaqAccordion
      @categorySelect="onCategorySelect"
      @itemSelect="onItemSelect"
      :items="myItems"
    />

  </div>

</template>

<script>
import VueFaqAccordion from 'src/components/vue-faq-accordion'
export default {
  name: 'PageHelp',
  components: {
    VueFaqAccordion
  },
  data () {
    return {
      myItems: [
        /**
          * General related questions
        */
        {
          title: 'What is groupThinq?',
          value: 'groupThinq is a decision making engine for any group of people to use to facilitate a vote',
          category: 'General Questions'
        },
        {
          title: 'What are the different kind of things I can do in the groupThinq application?',
          value: "While the function of this application is focused on setting up a vote for something. The boundaries of what you can do is only limited by imagination. Where do you want to go to lunch? What should your D&D campaign be about? Which Jonas brother really is the best? Any question you've ever had can be put up to a vote",
          category: 'General Questions'
        },
        /**
          * Account related questions
        */
        {
          title: "What happens to all of the in-app activity I've done if I delete my account?",
          value: 'The decisions that you have created and the votes submitted by you will be removed so as not affect ongoing ballots',
          category: 'Account Questions'
        },
        {
          title: 'How can I change my password?',
          value: 'Do we support that?',
          category: 'Account Questions'
        },
        {
          title: 'Can I update all of my information on the accounts page?',
          value: "You can update most of your user information on the 'My Account' page",
          category: 'Account Questions'
        },
        /**
          * Decision related questions
        */
        {
          title: 'How do I create a decision?',
          value: "Select the 'New Decision' page and follow the easy to use sheet to fill out your decision",
          category: 'Decision Questions'
        },
        {
          title: 'What is a ranked pair vote?',
          value: 'A ranked pair vote is where everyone can vote on multiple options. Your first pick has the most weight and so on. Which ever option has the highest votes, accounting for vote weight, wins',
          category: 'Decision Questions'
        },
        {
          title: "What does the 'first past the vote' option mean?",
          value: 'This is the standard vote process many are accustomed to. Simply make a vote and the option with the most at the end wins',
          category: 'Decision Questions'
        },
        {
          title: 'Can I change my vote before the decision deadline?',
          value: 'You can update your vote as many times as you want before the deadline, but be careful. Only your final vote will count',
          category: 'Decision Questions'
        },
        /**
          * Users related questions
        */
        {
          title: 'What users can see a decision I create?',
          value: 'Only users that you have added to a decision are able to see that decision',
          category: 'User Questions'
        },
        {
          title: 'Can I hide a decision from specific users?',
          value: 'Any users that you do not include in your decision will not be able to see it',
          category: 'User Questions'
        },
        {
          title: 'Does someone need to have a groupThinq account to be able to participate in a vote?',
          value: 'All participants must have active groupThinq accounts. Look out in the future for guest accounts though',
          category: 'User Questions'
        },
        {
          title: 'How many users can I add to a decision?',
          value: 'You are only limited by the number of active users on groupThinq',
          category: 'User Questions'
        }
      ]
    }
  },
  methods: {
    onCategorySelect (args) {
      console.log('onCategorySelect', args)
    },
    onItemSelect (args) {
      console.log('onItemSelect', args)
    }
  }
}
</script>
