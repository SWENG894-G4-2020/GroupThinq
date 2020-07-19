<template>
    <q-page padding class="fit column items-center">
      <div v-if="isLoaded">
        <div class="text-h5 text-primary text-center" v-if="decisionList.length == 0">No results yet.</div>
        <div class="row q-gutter-md">
          <ResultsCard
            v-for="decision in decisionList"
            :key="decision.name"
            v-bind:ballot="decision.ballots[0]"
            v-bind:decisionInfo="{ name: decision.name, description: decision.description }"
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
    </q-page>
</template>

<script>
import auth from 'src/store/auth'
import ResultsCard from 'src/components/ResultsCard'

export default {
  name: 'PageResults',

  components: {
    ResultsCard
  },

  data () {
    return {
      isLoaded: false,
      isError: false,
      decisionList: []
    }
  },

  mounted () {
    this.getData()
  },

  methods: {
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
