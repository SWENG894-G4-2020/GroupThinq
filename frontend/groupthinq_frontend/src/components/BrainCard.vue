<template>
  <q-card bordered style="height: 100%">
    <q-card-section>
      <div class="text-h5"><q-icon color="accent" name="psychology" /> Groupthinq Brain&trade;</div>
    </q-card-section>
    <q-card-section v-if="isLoaded">
      <div class="text-grey-7 q-pa-sm">Our machine learning model perdicts the following results for <span class="text-bold">"{{decision.name}}"</span></div>
      <div class="bg-grey-2 q-pa-sm" style="width: 100%">
        <div v-for="(result, idx) in tabulatedResults" :key="idx" class="q-pa-sm">
          <div class="row">
            <div class="q-pa-xs"><q-icon name="done" class="text-green q-pa-xs" v-if="result.winner"/>{{ result.title }}</div>
            <div class="col-grow q-pa-xs" style="text-align: right">{{ Math.round(result.percentage * 100)}}% </div>
          </div>
          <div class="q-pa-xs"><q-linear-progress rounded color="accent" size="14px" :value="result.percentage" /></div>
        </div>
      </div>
    </q-card-section>
    <q-card-section v-else-if="!isError">
      <div class="text-h5 text-primary">Thinking...
        <q-spinner-gears color="primary" size="2em"/>
      </div>
    </q-card-section>
    <q-card-section v-else>
      <div class="text-h5 text-negative self-center">
        Something went wrong. <q-icon name="warning" />
      </div>
      <div v-if="errorMsg" class="text-h7 text-negative self-center">{{errorMsg}}</div>
    </q-card-section>
  </q-card>
</template>

<script>
import tfmodel from 'src/store/tfmodel'

export default {
  name: 'BrainCard',

  data () {
    return {
      results: '',
      isLoaded: false,
      isError: false,
      errorMsg: '',
      options: []
    }
  },

  props: {
    decision: {
      type: Object,
      required: true
    }
  },

  mounted () {
    this.getML(this.decision)
  },

  watch: {
    decision: function (newDec, oldDec) {
      this.isError = false
      this.isLoaded = false
      this.errorMsg = false
      this.results = ''
      this.getML(newDec)
    }
  },

  computed: {
    tabulatedResults: function () {
      const data = []
      let iter = 0
      this.options.forEach(opt => {
        data.push({
          title: opt.title,
          percentage: this.results[iter],
          winner: false
        })
        iter++
      })
      data.sort((a, b) => b.percentage - a.percentage)
      data[0].winner = true
      return data
    }
  },

  methods: {
    async getML (decision) {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/decision/${this.decision.id}`)
        const input = []
        input.push(response.data.data[0].name)
        this.options = response.data.data[0].ballots[0].ballotOptions
        this.options.forEach(opt => input.push(opt.title))
        this.results = await tfmodel.getSimilarityScores(input)
        this.isLoaded = true
      } catch (error) {
        this.error = true
        this.errorMsg = error.message
        console.log(error)
        this.$emit('error')
      }
    }
  }
}
</script>
