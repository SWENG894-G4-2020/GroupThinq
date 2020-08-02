<template>
  <div class="q-pa-md full-width">
    <div v-if="isLoaded" class="row">
      <div class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered style="height: 100%">
          <q-card-section>
            <div class="text-h5"><q-icon name="how_to_vote" /> Decision Details</div>
            <q-input
            :readonly="(mode === 'create' || mode === 'edit') ? false : true"
            name="name-input"
            v-model="decision.name"
            label="What should we decide?"
            :rules="[val => !!val || '*Required', val => val.length < 161 || 'Decision name must be less than 160 characters']"
            />
            <q-expansion-item
              icon="comment"
              dense
              :label="(mode === 'create' || mode === 'edit') ? 'Add additional details' : 'View additional details'"
              caption="Optionally add any additional information for decision participants"
            >
              <q-input
              :readonly="(mode === 'create' || mode === 'edit') ? false : true"
              name="description-input"
              autogrow
              dense
              :filled="!(mode === 'create' || mode === 'edit') ? false : true"
              clearable
              class="text-body2 text-grey-5"
              placeholder="No Details"
              v-model="decision.description"
              />
            </q-expansion-item>
          </q-card-section>
          <q-card-section>
            <div class="text-h5"><q-icon name="person" /> Participants</div>
            <q-select
              label="Select Participants"
              name="user-select"
              hint="Who can vote on this decision?"
              :readonly="(mode === 'create' || mode === 'edit') ? false : true"
              v-model="selectedUsers"
              use-input
              use-chips
              multiple
              input-debounce="0"
              :options="filteredUsersList"
              @filter="filterFn"
            />
          </q-card-section>
        </q-card>
      </div>
      <div v-if="!expired || mode === 'create'" class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered style="height: 100%">
          <q-card-section class="q-pb-sm">
            <div class="text-h5"><q-icon name="ballot" /> Ballot</div>
            <q-input
            v-model="datetime"
            name="datetime-input"
            label="Voting end date"
            :rules="[val => checkValidDate(val) || '*Valid Date Required']"
            :readonly="(mode === 'create' || mode === 'edit') ? false : true"
            mask="datetime"
            hint="YYYY/MM/DD HH:mm">
              <template v-if="(mode === 'create' || mode === 'edit')" v-slot:append>
                <q-icon name="event" class="cursor-pointer" @click="openDatetimeDialog()">
                  <q-dialog v-model="pickDatetimeDialog">
                      <q-card class="date-picker">
                        <q-card-section>
                          <div class="q-gutter-sm row justify-center">
                            <q-date today-btn v-model="datetime" mask="YYYY/MM/DD HH:mm" default-year-month="2020/08" />
                            <q-time now-btn v-model="datetime" mask="YYYY/MM/DD HH:mm" />
                          </div>
                        </q-card-section>
                        <q-card-actions align="right">
                          <q-btn color="positive" @click="closeDatetimeDialog()" label="Confirm" />
                        </q-card-actions>
                      </q-card>
                  </q-dialog>
                </q-icon>
              </template>
            </q-input>
            <div v-if="mode === 'view'"><q-icon name="alarm_on" class="text-positive" /> {{daysRemaining}}d {{hoursRemaining}}h {{minutesRemaining}}m {{secondsRemaining}}s remaining</div>
            <div class="q-mt-sm">
              <div class="text-grey-8" style="font-size: 16px"> Voting Method</div>
              <q-btn-toggle
                v-if="mode === 'create'"
                spread
                v-model="decision.ballots[0].ballotTypeId"
                toggle-color="primary"
                :options="ballotTypeOptions"
              />
              <div v-else class="q-py-sm">{{ ballotTypeOptions.find(bt => bt.value === decision.ballots[0].ballotTypeId ).label }}</div>
              <div class="q-py-sm text-grey-7" style="min-height: 62px">{{ ballotTypeOptions.find(bt => bt.value === decision.ballots[0].ballotTypeId ).description }}</div>
            </div>
          </q-card-section>
          <q-card-section class="q-pt-none">
            <div class="text-grey-8" style="font-size: 16px"> Ballot choices</div>
            <q-input class="q-mb-md" v-model="newOption.title" label="Add Choice" >
              <template v-slot:append>
                <q-btn dense color="positive" icon="add" @click="addDecisionOption()" />
              </template>
            </q-input>
            <div class="column">
              <div v-for="(option,idx) in sortedOptions" :key="idx" class="row items-center q-mb-sm" style="width: 100%">
                <q-checkbox v-if="decision.ballots[0].ballotTypeId === 1 && mode !== 'create'" v-model="option.rank" />
                <q-avatar v-else-if="mode !== 'create'" size="md">{{ option.rank ? option.rank : idx + 1 }}</q-avatar>
                <span class="text-body1 col-grow">{{option.title}}</span>
                <q-btn v-if="decision.ballots[0].ballotTypeId === 2 && mode !== 'create'" flat color="positive" icon="expand_less" @click="removeDecisionOption(option)" />
                <q-btn v-if="decision.ballots[0].ballotTypeId === 2 && mode !== 'create'" flat color="negative" icon="expand_more" @click="removeDecisionOption(option)" />
                <q-btn v-if="mode === 'create'" flat color="negative" icon="close" @click="removeDecisionOption(option)" />
              </div>
            </div>
            <div class="row reverse q-gutter-sm">
              <q-btn v-if="!expired && mode === 'view'" icon="check" color="primary" label="Vote" />
            </div>
          </q-card-section>
        </q-card>
      </div>
      <div v-else class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered style="height: 100%">
          <q-card-section>
            <div class="text-h5"><q-icon name="poll" /> Results</div>
            <div><q-icon name="alarm_off" class="text-negative" /> Decided on {{ prettyDate }}</div>
          </q-card-section>
        </q-card>
      </div>
      <div v-if="!expired && mode === 'view'" class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered style="height: 100%">
          <q-card-section>
            <div class="text-h5"><q-icon name="psychology" /> Groupthinq Brain&trade;</div>
            <div>Perdiction: Option 1</div>
          </q-card-section>
        </q-card>
      </div>
      <div class="q-pa-sm col-xs-12">
        <q-banner v-if="!submissionValid" class="bg-red-1 q-my-sm">
          <template v-slot:avatar>
            <q-icon name="warning" color="negative" />
          </template>
          A new decision requires a title, valid expiration date, and at least one option.
        </q-banner>
        <div class="row reverse q-gutter-sm">
          <q-btn v-if="mode === 'create'" icon="add" color="positive" label="Create" @click="onCreate()" :loading="submitting">
            <template v-slot:loading>
              <q-spinner />
            </template>
          </q-btn>
          <q-btn v-if="!expired && mode === 'edit'" icon="check" color="positive" label="Confirm" />
          <q-btn v-if="!expired && mode === 'view'" icon="edit" label="Edit" />
          <q-btn v-if="!expired && (mode === 'view' || mode === 'edit')" icon="delete" color="negative" label="Delete" />
          <q-btn v-if="!expired && mode === 'edit'" icon="clear" label="Cancel" />
          <q-btn v-if="mode === 'create'" icon="clear" label="Cancel" to="/main" />
        </div>
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
      submissionValid: true,
      decision: { ballots: [{ ballotTypeId: 1, ballotOptions: [] }], includedUsers: [{ userName: this.currentUserName }] },
      savedDecision: { ballots: [{ ballotTypeId: 1, ballotOptions: [] }], includedUsers: [{ userName: this.currentUserName }] },
      newOption: { title: '', userName: this.currentUserName },
      datetime: '',
      resultsList: [],
      allUsersList: [],
      filteredUsersList: [],
      selectedUsers: [],
      pickDatetimeDialog: false,
      daysRemaining: '',
      hoursRemaining: '',
      minutesRemaining: '',
      secondsRemaining: '',
      submitting: false,
      ballotTypeOptions: [
        {
          label: 'First Past the Post',
          value: 1,
          description: 'The choice with the most votes win. Voters pick one choice.'
        },
        {
          label: 'Ranked Pair',
          value: 2,
          description: 'Selects a single winner using votes that express preferences. Voters rank the choices.'
        }
      ]
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
    this.getAllUsers()
    this.calculateRemainingTime()
  },

  computed: {
    expired: function () {
      const diff = (new Date(this.datetime) - Date.now()) / 1000
      if (diff < 0) { return true }
      return false
    },

    prettyDate: function () {
      return new Date(this.datetime).toLocaleString()
    },

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

    sortedOptions: function () {
      return this.decision.ballots[0].ballotOptions
    }
  },

  methods: {
    setInitialMode () {
      if (this.id === 'new') {
        this.mode = 'create'
        this.isLoaded = true
      } else {
        this.mode = 'view'
        this.getDecision()
      }
    },

    isMode (mode) {
      if (this.mode === mode) {
        return true
      }
      return false
    },

    async getDecision () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/decision/${this.id}`)

        if (response.data.data.length < 1) {
          throw new Error('No Decision Found')
        }

        this.decision = response.data.data[0]
        this.decision.includedUsers.forEach((user) => this.selectedUsers.push(user.userName))
        this.datetime = this.decision.ballots[0].expirationDate

        if (this.expired) {
          this.getResultsData()
        }

        this.isLoaded = true
      } catch (error) {
        console.log(error)
        this.isError = true
        this.errorMsg = error.message
      }
    },

    checkValidDate (d) {
      const check = Date.parse(d)
      if (check) { return true }
      return false
    },

    checkValidSubmit () {
      if (this.decision.name === '') { return false }
      if (!this.checkValidDate(this.datetime)) { return false }
      if (!this.decision.ballots[0].ballotTypeId || this.decision.ballots[0].ballotTypeId === '') { return false }
      if (this.decision.ballots[0].length < 1) { return false }
      return true
    },

    closeDatetimeDialog () {
      this.pickDatetimeDialog = false
    },

    openDatetimeDialog () {
      this.pickDatetimeDialog = true
    },

    async getAllUsers () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/users`)
        response.data.data.forEach((user) => this.allUsersList.push(user.userName))
      } catch (error) {
        console.log(error)
        this.errorMsg = error.message
        this.$emit('error')
      }
    },

    addDecisionOption () {
      if (this.newOption.title !== '') {
        this.decision.ballots[0].ballotOptions.push({ title: this.newOption.title, userName: this.currentUserName })
        this.newOption = { title: '', userName: this.currentUserName }
      }
    },

    removeDecisionOption (option) {
      const pos = this.decision.ballots[0].ballotOptions.indexOf(option)
      this.decision.ballots[0].ballotOptions.splice(pos, 1)
    },

    filterFn (val, update, abort) {
      if (val.length < 3) {
        abort()
        return
      }
      update(() => {
        const needle = val.toLowerCase()
        this.filteredUsersList = this.allUsersList
          .filter(v => v !== this.currentUserName)
          .filter(v => v.toLowerCase().indexOf(needle) > -1)
      })
    },

    calculateRemainingTime () {
      const secondsTiemr = setInterval(() => {
        const diff = (new Date(this.decision.ballots[0].expirationDate) - Date.now()) / 1000

        if (diff < 0) {
          clearInterval(secondsTiemr)
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

    async getResultsData () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/ballot/${this.decision.ballots[0].id}/results`)
        this.resultsList = response.data.data
      } catch (error) {
        console.log(error)
      }
    },

    async onCreate () {
      if (!this.checkValidSubmit()) {
        this.submissionValid = false
        return
      }
      this.submitting = true
      this.decision.ballots[0].expirationDate = new Date(this.datetime)
      this.selectedUsers.forEach((user) => this.decision.includedUsers.push({ userName: user }))
      this.decision.includedUsers.push({ userName: this.currentUserName })
      this.decision.ownerUsername = this.currentUserName

      console.log(this.decision)

      try {
        const response = await this.$axios.post(`${process.env.BACKEND_URL}/decision/`, this.decision)
        this.decision = response.data.data[0]
        this.selectedUsers = []
        this.includedUsers.forEach((user) => this.selectedUsers.push(user.userName))
        this.submitting = false
        this.submissionValid = true
      } catch (error) {
        console.log(error)
        this.$emit('error')
      }
    }
  }
}
</script>

<style>
.q-field--readonly .q-field__control:before {
    border-bottom-style: none !important;
}

.option-desc {
  font-size: 0.85em;
  font-style: italic;
  max-width: 200px;
  white-space: normal;
  color: #555;
  margin-top: 4px;
}
</style>
