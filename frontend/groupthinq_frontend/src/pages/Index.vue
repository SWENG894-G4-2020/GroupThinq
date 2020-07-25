<template>
    <q-page padding class="fit column items-stretch">
      <div v-if="isLoaded">
        <div class="full-width row justify-between">
          <q-btn color="green-8" label="New Decision" icon-right="add" @click="createDecision()"/>
        </div>
        <div class="text-h5 text-primary text-center" v-if="decisionList.length == 0">No decisions? Make a new one!</div>
        <div class="row q-mt-md">
          <DecisionSummaryCard
            v-for="(decision, idx) in sortedDecisions"
            :key="idx"
            v-bind:decision="decision"
            class="col-xs-12 col-sm-6 col-lg-3"
            @needReload="getData()"
          />
        </div>
      </div>
      <div v-else-if="!isError">
        <div class="text-h5 text-primary">Loading...
          <q-spinner-hourglass color="primary" size="2em"/>
        </div>
      </div>
      <div v-else>
        <div class="text-h5 text-negative self-center">
          Something went wrong.
          <q-icon name="warning" />
        </div>
        <div class="text-h7 text-negative self-center">Please refresh the page.
        </div>
      </div>
    </q-page>
</template>

<script>
import auth from 'src/store/auth'
import DecisionSummaryCard from 'src/components/DecisionSummaryCard'

export default {
  name: 'PageIndex',

  components: {
    DecisionSummaryCard
  },

  data () {
    return {
      isLoaded: false,
      isError: false,
      decisionList: [],
      filterOptions: ['All', 'Open', 'Closed'],
      currentFilter: 'All',
      sortOptions: ['Recent', 'Oldest'],
      currentSort: 'Recent'
    }
  },

  computed: {
    sortedDecisions: function () {
      // Temporary solution for ballot - decision deletion display
      // Make sure to change tempDecisionList back to this.DecsionList when issue is resolved
      const tempDecisionList = []
      let decisionIterator
      for (decisionIterator of this.decisionList) {
        if (!decisionIterator.deleted) { tempDecisionList.push(decisionIterator) }
      }

      return tempDecisionList
    }
  },

  mounted () {
    this.getData()
  },

  methods: {
    createDecision () {
      this.createDecisionDialog = true
    },

    async getData () {
      try {
        const userName = auth.getTokenData().sub
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/user/${userName}/decisions`)
        this.decisionList = response.data.data
        this.isLoaded = true
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    }
  }

}
</script>
