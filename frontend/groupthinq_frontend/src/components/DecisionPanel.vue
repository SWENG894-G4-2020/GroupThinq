<template>
  <div class="q-pa-md full-width">
    {{ decision.name }}
  </div>
</template>

<script>
import auth from 'src/store/auth'

export default {
  name: 'DecisionPanel',

  data () {
    return {
      isLoaded: false,
      isError: false,
      currentUserName: '',
      mode: 'view',
      modeData: {
        view: {

        },
        edit: {

        },
        create: {

        }
      },
      decision: {},
      results: {}
    }
  },

  props: {
    id: {
      type: String,
      required: false
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.getDecision()
  },

  methods: {
    async getDecision () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/decision/${this.id}`)

        if (response.data.data.length < 1) {
          throw new Error('No Decision Found')
        }

        this.decision = response.data.data[0]
        this.isLoaded = true
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    }
  }
}
</script>
