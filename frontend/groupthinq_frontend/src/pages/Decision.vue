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
        <div class="q-pa-sm col-xs-12 col-md-6">
          <ResultsCard
            v-bind:ballot="this.decision.ballots[0]"
            v-bind:decisionInfo="{ name: this.decision.name, description: this.decision.description }"
           />
        </div>
      </div>
      <div class="q-pa-sm col-xs-12">
        <q-banner v-if="!submissionValid" class="bg-red-1 q-my-sm">
          <template v-slot:avatar>
            <q-icon name="warning" color="negative" />
          </template>
          A new decision requires a title, valid expiration date, and at least one option.
        </q-banner>
        <div class="row reverse q-gutter-sm">
          <q-btn v-if="(currentUserName === decision.ownerUsername || currentUserRole === 'Admin') && mode === 'edit'" class="col-xs-12 col-sm-auto" size="lg" icon="check" color="positive" label="Confirm" name="decision-confirm" @click="onEditConfirm()"/>
          <q-btn v-if="(currentUserName === decision.ownerUsername || currentUserRole === 'Admin') && mode === 'view'" class="col-xs-12 col-sm-auto" size="lg" icon="edit" label="Edit" name="decision-edit" @click="onEdit()"/>
          <q-btn v-if="currentUserName === decision.ownerUsername || currentUserRole === 'Admin'" class="col-xs-12 col-sm-auto" icon="delete" size="lg" color="negative" label="Delete" @click="deleteDecisionDialog = true" name="decision-delete"/>
          <q-btn v-if="(currentUserName === decision.ownerUsername || currentUserRole === 'Admin') && mode === 'edit'" class="col-xs-12 col-sm-auto" icon="close" size="lg" label="Cancel" name="decision-edit-cancel" @click="onEditCancel()"/>
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
import ResultsCard from 'src/components/ResultsCard'

export default {
  name: 'PageDecisions',

  components: {
    DecisionDetailsCard,
    BallotCard,
    ResultsCard
  },

  data () {
    return {
      currentUserName: '',
      currentUserRole: 'User',
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
    this.currentUserRole = auth.getTokenData().role
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

    onEdit () {
      this.mode = 'edit'
    },

    onEditCancel () {
      this.isLoaded = false
      this.mode = 'view'
      this.getDecision()
    },

    buildDecision () {
      const decision = this.$refs.details.getRequestObject()
      decision.ballots.push(this.$refs.ballot.getRequestObject())
      return decision
    },

    async onEditConfirm () {
      if (!this.$refs.details.isValid() || !this.$refs.ballot.isValid()) {
        this.submissionValid = false
        return
      }
      const decision = this.buildDecision()

      try {
        await this.$axios.put(`${process.env.BACKEND_URL}/decision/${this.decision.id}`, decision)
        this.onEditCancel()

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
