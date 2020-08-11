<template>
  <q-card bordered style="height: 100%">
    <q-card-section class="q-pa-md">
      <div class="text-h5 q-py-md"><q-icon name="poll" color="grey-7"/> Results</div>
      <div class="text-grey-7">Decision: {{decision.name}}</div>
      <div v-if="expired" class="text-caption">Decided on: {{this.prettyDate}}</div>
    </q-card-section>
    <q-card-section v-if="!expired" class="q-pa-md">
      <span class="text-caption">No results yet.</span>
    </q-card-section>
    <q-card-section class="q-pa-md column items-center" v-else-if="isLoaded">
      <div v-if="ballot.ballotTypeId === 1" class="bg-grey-2 q-pa-sm" style="width: 100%">
        <div v-for="(result, idx) in tabulatedResults" :key="idx" class="q-pa-sm">
          <div class="row">
            <div class="q-pa-xs"><q-icon name="done" class="text-green q-pa-xs" v-if="result.winner"/>{{ result.name }}</div>
            <div class="col-grow q-pa-xs" style="text-align: right">{{ Math.round(result.percentage * 100)}}% ({{ result.votes }})</div>
          </div>
          <div class="q-pa-xs"><q-linear-progress rounded size="14px" :value="result.percentage" /></div>
        </div>
      </div>
      <div v-else class="bg-grey-2 q-pa-sm" style="width: 100%">
        <div class=" text-h6 q-pa-xs">Winning Choice: <q-icon name="emoji_events" size="lg" class="text-green q-pa-xs"/>{{ resultsList[0].winner.title }}</div>
        <q-expansion-item
          icon="read_more"
          label="Details"
          caption="See each 1v1 result"
        >
          <div v-for="(result, idx) in rankedPairResults" :key="idx" class="q-pa-sm">
            <div class="q-pa-xs"> <span :class="(result.winner.id === resultsList[0].winner.id) ? 'text-positive text-bold': ''">{{ result.winner.title }}</span> beat <span :class="(result.loser.id === resultsList[0].winner.id) ? 'text-negative text-bold': ''">{{result.loser.title}}</span> by ({{result.margin}})</div>
            <q-separator />
          </div>
        </q-expansion-item>
      </div>
    </q-card-section>
    <q-card-section v-else>
      <div class="text-h5 text-primary">Loading...
        <q-spinner-hourglass color="primary" size="2em"/>
      </div>
    </q-card-section>
  </q-card>
</template>

<script>

export default {
  name: 'ResultsCard',

  data () {
    return {
      isLoaded: false,
      resultsList: [],
      ballot: {}
    }
  },

  mounted () {
    this.ballot = this.decision.ballots[0]
    this.getResultsData()
  },

  props: {
    decision: {
      type: Object,
      required: true
    }
  },

  computed: {
    resultTotals: function () {
      const resultTotals = {}
      let option
      for (option of this.ballot.ballotOptions) {
        resultTotals[option.id] = 0
      }

      let result
      for (result of this.resultsList) {
        resultTotals[result.ballotOptionId] += 1
      }
      return resultTotals
    },

    winnerId: function () {
      let winnerId = []
      let max = 0
      let resultId
      for (resultId in this.resultTotals) {
        if (this.resultTotals[resultId] > max) {
          max = this.resultTotals[resultId]
          winnerId = [parseInt(resultId)]
        } else if (this.resultTotals[resultId] === max) {
          winnerId.push(parseInt(resultId))
        }
      }
      return winnerId
    },

    tabulatedResults: function () {
      const data = []
      let option
      let total = 0
      for (option of this.ballot.ballotOptions) {
        data.push({
          name: option.title,
          votes: this.resultTotals[option.id],
          winner: this.winnerId.includes(option.id)
        })
        total = total + this.resultTotals[option.id]
      }
      data.forEach(r => {
        r.total = total
        if (r.total === 0) {
          r.percentage = 0
        } else {
          r.percentage = r.votes / r.total
        }
      })
      data.sort((a, b) => b.percentage - a.percentage)
      return data
    },

    rankedPairResults: function () {
      const data = []
      this.resultsList[0].rankedPairWinners.forEach(res => data.push(res))
      data.sort((a, b) => b.margin - a.margin)
      return data
    },

    expired: function () {
      const diff = (new Date(this.ballot.expirationDate) - Date.now()) / 1000
      if (diff < 0) { return true }
      return false
    },

    prettyDate: function () {
      return new Date(this.ballot.expirationDate).toGMTString()
    }
  },

  methods: {
    async getResultsData () {
      try {
        if (this.expired) {
          const response = await this.$axios.get(`${process.env.BACKEND_URL}/ballot/${this.ballot.id}/results`)
          this.resultsList = response.data.data
          this.isLoaded = true
        }
      } catch (error) {
        console.log(error)
      }
    }
  }
}
</script>

<style>
.option-desc {
  font-size: 0.85em;
  font-style: italic;
  max-width: 200px;
  white-space: normal;
  color: #555;
  margin-top: 4px;
}
</style>
