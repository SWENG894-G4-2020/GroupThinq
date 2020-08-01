<template>
  <div class="q-pa-md full-width">
    <div v-if="isLoaded" class="row">
      <div class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered>
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
            <q-input
            v-model="decision.ballots[0].expirationDate"
            name="datetime-input"
            label="When should voting close?"
            :rules="[val => checkValidDate(val) || '*Valid Date Required']"
            mask="datetime"
            hint="YYYY/MM/DD HH:mm">
              <template v-if="(mode === 'create' || mode === 'edit')" v-slot:append>
                <q-icon name="event" class="cursor-pointer" @click="openDatetimeDialog()">
                  <q-dialog v-model="pickDatetimeDialog">
                      <q-card class="date-picker">
                        <q-card-section>
                          <div class="q-gutter-sm row justify-center">
                            <q-date today-btn v-model="decision.ballots[0].expirationDate" mask="YYYY/MM/DD HH:mm" default-year-month="2020/08" />
                            <q-time now-btn v-model="decision.ballots[0].expirationDate" mask="YYYY/MM/DD HH:mm" />
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
          </q-card-section>
        </q-card>
      </div>
      <div class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered style="height: 100%">
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
      <div class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered>
          <q-card-section v-if="!expired">
            <div class="text-h5"><q-icon name="ballot" /> Ballot</div>
            <div v-if="mode === 'view'"><q-icon name="alarm_on" class="text-positive" /> {{daysRemaining}}d {{hoursRemaining}}h {{minutesRemaining}}m {{secondsRemaining}}s remaining</div>
            <div v-if="mode === create"></div>
            <div v-else> </div>
          </q-card-section>
          <q-card-section v-else>
            <div class="text-h5"><q-icon name="poll" /> Results</div>
            <div><q-icon name="alarm_off" class="text-negative" /> Decided on {{ prettyDate }}</div>
          </q-card-section>
        </q-card>
      </div>
      <div v-if="!expired && mode === 'view'" class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered>
          <q-card-section>
            <div class="text-h5"><q-icon name="psychology" /> Groupthinq Brain&trade;</div>
            <div>Perdiction: Option 1</div>
          </q-card-section>
        </q-card>
      </div>
      <div class="q-pa-sm col-xs-12 col-md-6">
        <q-card bordered>
          <q-card-section>
            <div class="text-h5"><q-icon name="psychology" /> Actions</div>
            <div>Perdiction: Option 1</div>
          </q-card-section>
        </q-card>
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
      decision: { ballots: [{}], includedUsers: [] },
      resultsList: [],
      allUsersList: [],
      filteredUsersList: [],
      selectedUsers: [],
      pickDatetimeDialog: false,
      daysRemaining: '',
      hoursRemaining: '',
      minutesRemaining: '',
      secondsRemaining: '',
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
      const diff = (new Date(this.decision.ballots[0].expirationDate) - Date.now()) / 1000
      if (diff < 0) { return true }
      return false
    },

    isOpen: function () {
      if (!this.expired) {
        return true
      } else {
        return false
      }
    },

    prettyDate: function () {
      return new Date(this.decision.ballots[0].expirationDate).toLocaleString()
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
    }
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
        this.decision.includedUsers.forEach((user) => this.selectedUsers.push(user.userName))

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
      if (!this.checkValidDate(this.decision.ballots[0].expirationDate)) { return false }
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

      this.decision.ballots[0].expirationDate = new Date(this.newExpirationDate)
      this.selectedUsers.forEach((user) => this.decision.includedUsers.push({ userName: user }))
      this.decision.ballots[0].ballotTypeId = this.ballotType
      this.decision.ballots[0].ballotOptions = this.optionsList

      console.log(this.newDecision)

      try {
        await this.$axios.post(`${process.env.BACKEND_URL}/decision/`, this.newDecision)
        this.$emit('createClose')
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
