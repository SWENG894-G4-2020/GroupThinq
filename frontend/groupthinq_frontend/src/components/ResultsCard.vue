<template>
  <q-card style="">
    <q-card-section class="q-pa-md column">
      <span class="text-h4">{{decisionInfo.name}}</span>
      <span class="text-subtitle1 text-grey">{{decisionInfo.description}}</span>
      <span class="text-caption">Decided on: {{this.prettyDate}}</span>
    </q-card-section>
    <q-card-section class="q-pa-md column items-center" v-if="resultsList">
      <ResultsTable
            v-bind:tabulatedResults="tabulatedResults"/>
    </q-card-section>
    <q-card-section v-else>
      No results to show.
    </q-card-section>
    <q-card-actions align="right" v-if="decisionInfo.showClose">
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
      isLoaded: false,
      resultsList: []
    }
  },

  mounted () {
    this.getResultsData()
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
      for (option of this.ballot.ballotOptions) {
        data.push({
          name: option.title,
          description: option.description,
          votes: this.resultTotals[option.id],
          winner: this.winnerId.includes(option.id)
        })
      }
      return data
    },

    prettyDate: function () {
      return new Date(this.ballot.expirationDate).toGMTString()
    }
  },

  methods: {
    async getResultsData () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/ballot/${this.ballot.id}/results`)
        this.resultsList = response.data.data
        this.isLoaded = true
      } catch (error) {
        console.log(error)
      }
    }
  },

  props: {
    decisionInfo: {
      type: Object,
      required: true
    },
    ballot: {
      type: Object,
      required: true
    }
  }
}
</script>
