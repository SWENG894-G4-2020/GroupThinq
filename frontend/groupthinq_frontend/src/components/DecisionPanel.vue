<template>
  <div class="q-pa-md full-width">
    <div v-if="isLoaded">
      <div>
        <q-input
        :readonly="(mode === 'create' || mode === 'edit') ? false : true"
        class="q-my-xs text-h5"
        name="decision-name"
        v-model="decision.name"
        label="Name"
        :rules="[val => !!val || '*Required', val => val.length < 161 || 'Decision name must be less than 160 characters']"
        />
        <q-input
        :readonly="(mode === 'create' || mode === 'edit') ? false : true"
        autogrow
        :filled="!(mode === 'create' || mode === 'edit') ? false : true"
        clearable
        class="q-my-md text-body2 text-grey-5"
        v-model="decision.description"
        label="Note (Optional)" />
      </div>
    </div>
    <div v-else-if="!isError">
      <div class="text-h5 text-primary">Loading...<q-spinner-hourglass color="primary" size="2em"/></div>
    </div>
    <div v-else>
      <div class="text-h5 text-negative self-center">
        Something went wrong. <q-icon name="warning" />
      </div>
      <div v-if="errorMsg" class="text-h7 text-negative self-center">{{errorMsg}}</div>
    </div>
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
      errorMsg: '',
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
    this.setInitialMode()
  },

  methods: {
    async setInitialMode () {
      if (this.id === 'new') {
        this.mode = 'create'
        this.isLoaded = true
      } else {
        this.mode = 'view'
        this.getDecision()
      }
    },

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
        this.errorMsg = error.message
      }
    }
  }
}
</script>

<style>
.q-field--readonly .q-field__control:before {
    border-bottom-style: none !important;
}
</style>
