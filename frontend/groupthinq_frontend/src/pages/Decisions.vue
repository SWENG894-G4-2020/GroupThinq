<template>
    <q-page padding class="fit column items-stretch">
      <div v-if="isLoaded">
        <div class="full-width row justify-between">
          <q-btn color="green-8" label="New Decision" icon-right="add" @click="createDecision()"/>
          {{ $route.params.id }}
          <DecisionPanel v-bind:id="$route.params.id" />
          <q-select
            outlined
            dense
            label="Sort Order"
            v-model="currentSort"
            :options="sortOptions" />
        </div>
        <div class="text-h5 text-primary text-center" v-if="decisionList.length == 0">No decisions? Make a new one!</div>
        <DecisionCard
          v-for="(decision, idx) in sortedDecisions"
          :key="idx"
          v-bind="decision"
          @needReload="getData()"
        />
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
      <q-dialog v-model="createDecisionDialog" persistent>
        <CreateDecisionCard @createClose="closeCreateModal()"/>
      </q-dialog>
    </q-page>
</template>

<script>
import auth from 'src/store/auth'
import DecisionCard from 'src/components/DecisionCard'
import CreateDecisionCard from 'src/components/CreateDecisionCard'
import DecisionPanel from 'src/components/DecisionPanel'

export default {
  name: 'PageDecisions',

  components: {
    DecisionCard,
    CreateDecisionCard,
    DecisionPanel
  },

  data () {
    return {
      isLoaded: false,
      isError: false,
      createDecisionDialog: false,
      decisionList: [],
      sortOptions: ['Expiration', 'Ownership'],
      currentSort: 'Expiration'
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

      if (this.currentSort === 'Ownership') {
        const expiryOrdered = tempDecisionList.slice().sort((a, b) => new Date(a.ballots[0].expirationDate) - new Date(b.ballots[0].expirationDate))
        const reordered = []
        expiryOrdered.forEach(element => {
          if (element.ownerUsername === auth.getTokenData().sub) {
            reordered.push(element)
          }
        })
        expiryOrdered.forEach(element => {
          if (element.ownerUsername !== auth.getTokenData().sub) {
            reordered.push(element)
          }
        })
        return reordered
      } else {
        return tempDecisionList.slice().sort((a, b) => new Date(a.ballots[0].expirationDate) - new Date(b.ballots[0].expirationDate))
      }
    }
  },

  mounted () {
    this.getData()
  },

  methods: {
    createDecision () {
      this.createDecisionDialog = true
    },

    closeCreateModal () {
      this.createDecisionDialog = false
      this.getData()
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
