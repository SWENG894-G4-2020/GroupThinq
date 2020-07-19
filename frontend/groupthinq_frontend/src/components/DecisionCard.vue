<template>
<div style="min-width: 100%">
  <q-card bordered class="q-mx-xl q-mt-md">
    <q-card-section>
      <div class="text-h5 row items-center">
        <div class="col q-my-md">{{name}}</div>
        <q-btn round icon="edit" class="col-auto" v-if="ownerUsername === this.currentUserName">
          <q-menu>
            <q-list>
              <q-item clickable v-close-popup @click="openEditModal()">
                <q-item-section>Edit</q-item-section>
              </q-item>
              <q-item clickable v-close-popup @click="deleteDecisionDialog = true">
                <q-item-section class="text-negative">DELETE</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>
      </div>
      <div class="text-grey">{{description}}</div>
      <div class="text-negative" v-if="!expired">Remaining: {{daysRemaining}}d {{hoursRemaining}}h {{minutesRemaining}}m {{secondsRemaining}}s</div>
      <div class="text-negative" v-else>Voting has closed.</div>
    </q-card-section>
    <q-card-actions class="row justify-between">
      <div class="col">
        <q-btn
            color="grey"
            round
            flat
            dense
            :icon="expanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
            @click="expanded = !expanded"
        />
        DETAILS
      </div>
      <div class="justify-end">
          <q-btn label="View Results"
            class="q-mx-xs"
            :outline="!status.results"
            color="primary"
            @click="resultsDialog = true"/>
          <!--
          :disable="!status.results"
          <q-btn label="Nominate"
            class="q-mx-xs"
            :outline="!status.nominate"
            :disable="!status.nominate"
            color="primary" />
            -->
          <q-btn
            :label="voteLabel"
            class="q-mx-xs"
            :outline="!status.vote"
            :disable="!status.vote"
            color="primary"
            @click="votingDialog = true" />
        </div>
    </q-card-actions>

    <q-slide-transition>
      <div v-show="expanded">
        <q-separator />
        <q-card-section horizontal>
            <q-card-section class="col-4 q-mx-md">
                <div class="text-overline">Owner</div>
                <div class="text-h7">{{ ownerUsername }}</div>
            <q-separator class="q-my-md" />
                <div class="text-overline">Members</div>
                <div class="text-h7 row" v-for="(user,idx) in includedUsers" :key="idx">{{ user.userName }}</div>
            </q-card-section>
            <q-card-section>
                <div class="text-overline">Voting Deadline</div>
                <div class="text-h7 row">{{ prettyDate }}</div>
                <q-separator class="q-my-md" />
                <div class="text-overline">Nomination Deadline</div>
                <div class="text-h7 row"> not implemented. </div>
            </q-card-section>
        </q-card-section>
      </div>
    </q-slide-transition>
  </q-card>
  <q-dialog v-model="editDecisionDialog" persistent style="width: 500px">
    <EditDecisionCard v-bind="editableDecision" @editClose="closeEditModal()"/>
  </q-dialog>
  <q-dialog v-model="deleteDecisionDialog" persistent>
    <q-card>
      <q-card-section class='column items-center'>
        <div class="text-grey-8"> Are you sure you want to delete this decision?</div>
        <div class="text-red-6"> This action cannot be undone!</div>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn label="cancel" @click="deleteDecisionDialog = false" />
        <q-btn color="red" @click="onConfirmDelete()" label="Confirm Deletion" />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="votingDialog" persistent style="width: 500px">
    <VotingCard
      v-bind:previousVote="previousVote"
      v-bind:ballotId="ballots[0].id"
      v-bind:ballotOptions="ballots[0].ballotOptions"
      v-bind:title="name"
      v-bind:description="description"
      @votingClose="closeVotingModal()" />
  </q-dialog>
  <q-dialog v-model="resultsDialog" persistent style="width: 500px">
    <ResultsCard
      v-bind:ballotOptions="ballots[0].ballotOptions"
      v-bind:results="resultsList"
      v-bind:decision="{ name: name, description: description }"
      @resultsClose="resultsDialog = false" />
  </q-dialog>
</div>
</template>

<script>
import auth from 'src/store/auth'
import EditDecisionCard from 'src/components/EditDecisionCard'
import VotingCard from 'src/components/VotingCard'
import ResultsCard from 'src/components/ResultsCard'

export default {
  name: 'DecisionCard',

  components: {
    EditDecisionCard,
    VotingCard,
    ResultsCard
  },

  data () {
    return {
      currentUserName: '',
      resultsList: [],
      editDecisionDialog: false,
      deleteDecisionDialog: false,
      votingDialog: false,
      resultsDialog: false,
      expirationDate: '',
      expanded: false,
      expired: false,
      daysRemaining: '',
      hoursRemaining: '',
      minutesRemaining: '',
      secondsRemaining: ''
    }
  },

  computed: {
    status: function () {
      if (!this.expired) {
        return { vote: true, nominate: false, results: false }
      } else {
        return { vote: false, nominate: false, results: true }
      }
    },
    prettyDate: function () {
      return new Date(this.editableDecision.ballots[0].expirationDate)
    },

    voteLabel: function () {
      let result
      for (result of this.resultsList) {
        if (result.userName === this.currentUserName) { return 'View Vote' }
      }
      return 'Vote'
    },

    previousVote: function () {
      let result
      for (result of this.resultsList) {
        if (result.userName === this.currentUserName) {
          let option
          for (option of this.ballots[0].ballotOptions) {
            if (result.ballotOptionId === option.id) {
              return { title: option.title, description: option.description }
            }
          }
        }
      }
      return undefined
    },

    editableDecision: function () {
      let tempBallots = [{ expirationDate: '1970-01-01T00:01:00-00:00' }]
      if (this.ballots.length !== 0) {
        tempBallots = this.ballots
      }
      return {
        id: this.id,
        name: this.name,
        description: this.description,
        includedUsers: this.includedUsers,
        ballots: tempBallots
      }
    }
  },

  props: {
    id: {
      type: Number,
      required: true
    },

    name: {
      type: String,
      required: true
    },

    description: {
      type: String,
      default: ''
    },

    ballots: {
      type: Array,
      required: true
    },

    ownerUsername: {
      type: String,
      default: ''
    },

    includedUsers: {
      type: Array,
      default: function () {
        return [
          { userName: this.ownerUsername }
        ]
      }
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.getResultsData()
    this.calculateRemainingTime()
  },

  methods: {
    calculateRemainingTime () {
      const secondsTiemr = setInterval(() => {
        const diff = (new Date(this.editableDecision.ballots[0].expirationDate) - Date.now()) / 1000

        if (diff < 0) {
          clearInterval(secondsTiemr)
          this.expired = true
          return
        }

        const days = Math.floor(diff / (3600 * 24))
        const hours = Math.floor((diff % (3600 * 24)) / 3600)
        const minutes = Math.floor((diff % 3600) / 60)
        const seconds = Math.floor((diff % 60))
        this.daysRemaining = days
        this.hoursRemaining = hours
        this.minutesRemaining = minutes
        this.secondsRemaining = seconds
      }, 500)
    },

    openEditModal () {
      this.editDecisionDialog = true
    },

    closeEditModal () {
      this.editDecisionDialog = false
      this.$emit('needReload')
    },

    closeVotingModal () {
      this.votingDialog = false
      this.getResultsData()
      this.$emit('needReload')
    },

    async getResultsData () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/ballot/${this.ballots[0].id}/results`)
        this.resultsList = response.data.data
      } catch (error) {
        console.log(error)
      }
    },

    async onConfirmDelete () {
      try {
        await this.$axios.delete(`${process.env.BACKEND_URL}/decision/${this.id}`, this.editDetails)
        this.deleteDecisionDialog = false
        this.$emit('needReload')
      } catch (error) {
        console.log(error)
      }
    }
  }
}
</script>
