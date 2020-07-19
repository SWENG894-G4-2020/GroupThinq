<template>
  <q-card style="">
    <q-card-section class="q-pa-md column items-center">
      <span class="text-h4">{{decision.name}}</span>
      <span class="text-subtitle1 text-grey">{{decision.description}}</span>
    </q-card-section>
    <q-card-section class="q-pa-md column items-center" v-if="resultTotals">
      <ResultsTable
            v-bind:tabulatedResults="tabulatedResults"/>
    </q-card-section>
    <q-card-section v-else>
      No results to show.
    </q-card-section>
    <q-card-actions align="right">
      <q-btn label="Close" @click="$emit('resultsClose')" />
    </q-card-actions>
  </q-card>
</template>

<script>
import ResultsTable from 'src/components/ResultsTable'

export default {
  name: 'ResultsCard',

  components: {
    ResultsTable
  },

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
      for (option of this.ballotOptions) {
        data.push({
          name: option.title,
          description: option.description,
          votes: this.resultTotals[option.id],
          winner: this.winnerId.includes(option.id)
        })
      }
      return data
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
    },
    decision: {
      type: Object,
      required: true
    }
  }
}
</script>
