<template>
  <q-page class="q-pa-md full-width" >
    <div v-if="!isError">
      <div class="row">
        <div class="q-pa-sm col-xs-12 col-md-6">
          <DecisionDetailsCard ref="details" v-bind:mode="mode" />
        </div>
        <div class="q-pa-sm col-xs-12 col-md-6">
          <BallotCard ref="ballot" v-bind:mode="mode" />
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
          <q-btn icon="add" color="positive" label="Create" @click="onCreate()" :loading="submitting" :disabled="submitting">
            <template v-slot:loading>
              <q-spinner />
            </template>
          </q-btn>
          <q-btn icon="clear" label="Cancel" to="/main" />
        </div>
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
      mode: 'create',
      submitting: false,
      submissionValid: true,
      isError: false
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
  },

  methods: {
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
    }
  }
}
</script>
