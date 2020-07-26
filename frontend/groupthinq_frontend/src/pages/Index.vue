<template>
  <q-page padding class="fit column items-stretch">
    <div v-if="isLoaded">
      <div class="row">
        <div class="col-grow q-pa-sm">
          <q-input
            v-model="search"
            debounce="500"
            filled
            placeholder="Search"
            >
            <template v-slot:append>
              <q-icon name="search" />
            </template>
          </q-input>
        </div>
        <div class="q-pa-sm col-xs-12 col-md-3 col-xl-2">
          <q-select
            outlined
            label="Sort"
            v-model="currentSort"
            :options="sortOptions"
            />
        </div>
      </div>
        <div class="text-h5 text-primary text-center" v-if="decisionList.length == 0">No decisions? Make a new one!</div>
        <div class="text-caption text-grey-7 q-pa-sm">{{sortedDecisions.length}} decisions found</div>
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
      <q-page-sticky position="bottom-right" :offset="[18, 18]">
        <q-btn fab icon="add" color="primary" label="Decision" @click="goToNewDecision()" />
      </q-page-sticky>
    </q-page>
</template>

<script>
import auth from 'src/store/auth'
import DecisionSummaryCard from 'src/components/decision/DecisionSummaryCard'

export default {
  name: 'PageIndex',

  components: {
    DecisionSummaryCard
  },

  data () {
    return {
      currentUserName: '',
      isLoaded: false,
      isError: false,
      decisionList: [],
      sortOptions: [
        {
          label: 'Upcoming Votes',
          value: 'upcoming'
        },
        {
          label: 'Recently Closed',
          value: 'recent'
        },
        {
          label: 'My Decisions',
          value: 'mine'
        }
      ],
      currentSort: { label: 'Upcoming Votes', value: 'upcoming' },
      search: ''
    }
  },

  computed: {
    sortedDecisions: function () {
      // Temporary solution for ballot - decision deletion display
      // Make sure to change tempDecisionList back to this.DecsionList when issue is resolved
      const active = []
      // const expired = []
      let decisionIterator
      for (decisionIterator of this.decisionList) {
        if (!decisionIterator.deleted) {
          active.push(decisionIterator)
        }
      }

      return active
    },


    decisionCounts: function () {
      const result = {
        total: 0,
        open: 0,
        closed: 0,
        mine: 0
      }

      this.decisionList.forEach(decision => {
        result.total++

        if (this.isDecisionExpired(decision)) {
          result.open++
        } else {
          result.closed++
        }

        if (decision.ownerUsername === this.currentUserName) {
          result.mine++
        }
      })

      return result
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.getData()
  },

  methods: {
    createDecision () {
      this.createDecisionDialog = true
    },

    isDecisionExpired (decision) {
      const diff = (new Date(decision.ballots[0].expirationDate) - Date.now()) / 1000
      if (diff < 0) { return true }
      return false
    },

    toggleSort () {

    },

    goToNewDecision () {

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
