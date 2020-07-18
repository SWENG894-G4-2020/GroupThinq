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
      Results Graph Here
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
    winnerId: function () {
      const resultTotal = {}
      let result
      for (result of this.results) {
        if (!resultTotal[result.ballotOptionId]) { resultTotal[result.ballotOptionId] = 0 }
        resultTotal[result.ballotOptionId] += 1
      }
      let winnerId = 0
      let max = 0
      let resultId
      for (resultId in resultTotal) {
        if (resultTotal[resultId] > max) {
          max = resultTotal[resultId]
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
  },

  methods: {

  }
}
</script>
