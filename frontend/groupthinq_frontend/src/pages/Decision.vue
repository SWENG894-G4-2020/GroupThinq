<template>
  <q-page class="q-pa-md full-width" >
    <div v-if="isLoaded">
      <div class="row">
        <div class="q-pa-sm col-xs-12 col-md-6">
          <DecisionDetailsCard ref="details" v-bind:mode="mode" v-bind:decision="decision" />
        </div>
        <div class="q-pa-sm col-xs-12 col-md-6">
          <BallotCard ref="ballot" v-bind:mode="mode" v-bind:decision="decision"/>
        </div>
      </div>
      <div class="q-pa-sm col-xs-12">
        <q-banner v-if="!submissionValid" class="bg-red-1 q-my-sm">
          <template v-slot:avatar>
            <q-icon name="warning" color="negative" />
          </template>
          A new decision requires a title, valid expiration date, and at least one option.
        </q-banner>
        <div class="row q-gutter-sm">
          <q-btn icon="arrow_back" color="primary" label="Back" to="/main" name="decision-back"/>
          <q-btn icon="delete" color="negative" label="Delete" @click="deleteDecisionDialog = true" name="decision-delete"/>
        </div>
      </div>
      <q-dialog v-model="deleteDecisionDialog" persistent>
        <q-card>
          <q-card-section class='column items-center'>
            <div class="text-grey-8"> Are you sure you want to delete this decision?</div>
            <div class="text-red-6"> This action cannot be undone!</div>
          </q-card-section>
          <q-card-actions align="right">
            <q-btn label="cancel" @click="deleteDecisionDialog = false" name="decision-delete-cancel"/>
            <q-btn color="negative" @click="onConfirmDelete()" label="Confirm Deletion" name="decision-delete-confirm"/>
          </q-card-actions>
        </q-card>
      </q-dialog>
    </div>
    <div v-else-if="!isError">
      <div class="text-h5 text-primary">Loading...
        <q-spinner-hourglass color="primary" size="2em"/>
      </div>
    </div>
    <div v-else>
      <div class="text-h5 text-negative self-center">
        Something went wrong. <q-icon name="warning" />
      </div>
      <div v-if="errorMsg" class="text-h7 text-negative self-center">{{errorMsg}}</div>
    </div>
  </q-page>
</template>

<script>
import auth from 'src/store/auth'
import DecisionDetailsCard from 'src/components/DecisionDetailsCard'
import BallotCard from 'src/components/BallotCard'

export default {
  name: 'PageDecisions',

  components: {
    DecisionDetailsCard,
    BallotCard
  },

  data () {
    return {
      currentUserName: '',
      mode: 'view',
      submitting: false,
      submissionValid: true,
      isError: false,
      errorMsg: '',
      isLoaded: false,
      deleteDecisionDialog: false,
      decision: {}
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.getDecision()
  },

  methods: {
    async getDecision () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/decision/${this.$route.params.id}`)

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
    },

    buildDecision () {
      const decision = {
        ballots: [{
          ballotTypeId: 1,
          ballotOptions: [],
          expirationDate: new Date(this.$refs.ballot.getExpiration())
        }],
        includedUsers: [{ userName: this.currentUserName }],
        name: this.$refs.details.name,
        description: this.$refs.details.description,
        ownerUsername: this.currentUserName
      }
      this.$refs.details.selectedUsers.forEach(user => decision.includedUsers.push({ userName: user }))
      this.$refs.ballot.options.forEach(option => decision.ballots[0].ballotOptions.push(option))
      return decision
    },

    async onCreate () {
      if (!this.$refs.details.isValid() || !this.$refs.ballot.isValid()) {
        console.log(this.buildDecision())
        this.submissionValid = false
        return
      }
      this.submitting = true
      const decision = this.buildDecision()

      try {
        await this.$axios.post(`${process.env.BACKEND_URL}/decision/`, decision)
        this.$router.push({ path: '/main' })

        this.submitting = false
        this.submissionValid = true
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    },

    async onConfirmDelete () {
      try {
        await this.$axios.delete(`${process.env.BACKEND_URL}/decision/${this.decision.id}`)
        this.deleteDecisionDialog = false
        this.$router.push({ path: '/main' })
      } catch (error) {
        console.log(error)
        this.isError = true
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
