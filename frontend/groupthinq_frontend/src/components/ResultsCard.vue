<template>
  <q-card bordered style="height: 100%">
    <q-card-section class="q-pa-md">
      <div class="text-h5 q-py-md"><q-icon name="poll" color="grey-7"/> {{decisionInfo.name}}</div>
      <span class="text-caption">Decided on: {{this.prettyDate}}</span>
    </q-card-section>
    <q-card-section class="q-pa-md column items-center" v-if="resultsList">
      <div class="text-h5" style="text-align: left; width: 100%">Results</div>
      <q-separator class="q-mb-md"/>
      <div class="bg-grey-2 q-pa-sm" style="width: 100%">
        <div v-for="result in tabulatedResults" :key="result.rank" class="q-pa-sm">
          <div class="row">
            <div class="q-pa-xs"><q-icon name="done" class="text-green q-pa-xs" v-if="result.winner"/>{{ result.name }}</div>
            <div class="col-grow q-pa-xs" style="text-align: right">{{ Math.round(result.percentage * 100)}}% ({{ result.votes }})</div>
          </div>
          <div class="q-pa-xs"><q-linear-progress rounded size="14px" :value="result.percentage" /></div>
        </div>
      </div>
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

export default {
  name: 'ResultsCard',

  data () {
    return {
      isLoaded: false,
      resultsList: [],
      initialPagination: {
        sortBy: 'desc',
        descending: false,
        page: 1,
        rowsPerPage: 10
      },
      columns: [
        {
          name: 'name',
          required: true,
          label: 'Option',
          align: 'left',
          field: row => row.name,
          format: val => `${val}`,
          sortable: true
        },
        { name: 'winner', align: 'center', label: 'Winner', field: 'winner', sortable: true },
        { name: 'votes', align: 'center', label: '# of Votes', field: 'votes', sortable: true }
      ]
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
