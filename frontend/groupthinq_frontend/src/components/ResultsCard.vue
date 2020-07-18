<template>
  <q-card style="">
    <q-card-section class="q-pa-md column items-center" v-if="winnerInfo">
      The winner is:
      {{winnerInfo.title}}
      {{winnerInfo.description}}
    </q-card-section>
    <q-card-section v-else>
      No results to show.
    </q-card-section>
    <q-card-section class="q-pa-md column items-center" >
      {{ballotOptions}}
      {{resultTotals}}
    </q-card-section>
    <q-card-actions align="right">
      <q-btn label="Close" @click="$emit('resultsClose')" />
    </q-card-actions>
  </q-card>
</template>

<script>
export default {
  name: 'ResultsCard',
  data () {
    return {
      voteResult: '',
      selectionError: false
    }
  },

  computed: {
    resultTotals: function () {
      const resultTotals = {}
      let option
      for (option of this.ballotOptions) {
        resultTotals[option.id] = 0
      }

      let result
      for (result of this.results) {
        resultTotals[result.ballotOptionId] += 1
      }
      return resultTotals
    },

    winnerId: function () {
      let winnerId = 0
      let max = 0
      let resultId
      for (resultId in this.resultTotals) {
        if (this.resultTotals[resultId] > max) {
          max = this.resultTotals[resultId]
          winnerId = parseInt(resultId)
        }
      }
      return winnerId
    },

    winnerInfo: function () {
      return this.ballotOptions.find(option => option.id === this.winnerId)
    }
  },

  props: {
    ballotOptions: {
      type: Array,
      required: true
    },
    results: {
      type: Array,
      required: true
    }
  }
}
</script>
