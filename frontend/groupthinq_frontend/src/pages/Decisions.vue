<template>
    <q-page padding class="column">
      <div v-if="isLoaded" class="column items-center">
        <q-btn color="green-8" label="New Decision" icon-right="add"  class="self-start" @click="createDecision()"/>
        <div class="text-h5 text-primary" v-if="decisionList.length == 0">No decisions? Make a new one!</div>
        <DecisionCard
          v-for="decision in decisionList.slice().sort((a,b) => new Date(a.ballots[0].expirationDate) - new Date(b.ballots[0].expirationDate))"
          :key="decision.name"
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

export default {
  name: 'PageDecisions',

  components: {
    DecisionCard,
    CreateDecisionCard
  },

  data () {
    return {
      isLoaded: false,
      isError: false,
      createDecisionDialog: false,
      decisionList: []
    }
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
  },

  mounted () {
    this.getData()
  }
}
</script>
