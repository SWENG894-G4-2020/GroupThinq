<template>
  <q-card style="width:50%; min-width:380px">
    <q-card-section class='column items-center q-pa-lg'>
      <div class="text-h5 q-ma-md"> Create A New Decision </div>
      <div class="text-subtitle2"> Decision Details
        <q-btn
          color="grey"
          round
          flat
          dense
          :icon="detailsExpanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
          @click="detailsExpanded = !detailsExpanded" />
      </div>
      <q-slide-transition>
        <div v-show="detailsExpanded" style="width:100%">
          <q-input class="q-my-xs text-h5" style="width: 100%" v-model="newDecision.name" label="Title" :rules="[val => !!val || '*Required']" />
          <q-input autogrow clearable type="textarea" class="q-my-md text-body2 text-grey-5" style="width: 100%; max-height: 6em" v-model="newDecision.description" label="Description (Optional)" />
          <q-input v-model="newExpirationDate" label="Expiration Date" :rules="[val => checkValidDate(val) || '*Valid Date Required']" mask="datetime" style="width: 100%" hint="YYYY/MM/DD HH:mm">
            <template v-slot:append>
              <q-icon name="event" class="cursor-pointer" @click="openDatetimeDialog()">
                <q-dialog v-model="pickDatetimeDialog">
                    <q-card class="date-picker">
                      <q-card-section>
                        <div class="q-gutter-sm row justify-center">
                          <q-date today-btn v-model="newExpirationDate" mask="YYYY/MM/DD HH:mm" default-year-month="2020/07" />
                          <q-time now-btn v-model="newExpirationDate" mask="YYYY/MM/DD HH:mm" />
                        </div>
                      </q-card-section>
                      <q-card-actions align="right">
                        <q-btn color="green-8" @click="closeDatetimeDialog()" label="Confirm" />
                      </q-card-actions>
                    </q-card>
                </q-dialog>
              </q-icon>
            </template>
          </q-input>
          <div class="q-py-md">
            <div class="text-grey-8" style="font-size: 16px"> Voting Method</div>
            <q-btn-toggle
              spread
              v-model="ballotType"
              toggle-color="primary"
              :options="ballotTypeOptions"
            />
            <div class="q-py-sm text-grey-7" style="min-height: 62px">{{ ballotTypeOptions.find(bt => bt.value === ballotType ).description }}</div>
          </div>
        </div>
      </q-slide-transition>
    </q-card-section>
    <q-separator />
    <q-card-section class='column items-center q-pa-md'>
      <div class="text-subtitle2"> Add Decision Participants (by username)
        <q-btn
          color="grey"
          round
          flat
          dense
          :icon="usersExpanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
          @click="usersExpanded = !usersExpanded" />
      </div>
      <q-slide-transition>
        <div v-show="usersExpanded" style="width:100%">
          <div class="row items-center">
            <div class="col">
              <q-select
                label="Select Participants"
                hint="Who can vote on this decision?"
                v-model="addedUsers"
                use-input
                use-chips
                multiple
                input-debounce="0"
                :options="filteredUsersList"
                @filter="filterFn"
                class="q-my-md"
                style="width: 100%"
              />
          </div>
        </div>
        </div>
      </q-slide-transition>
    </q-card-section>
    <q-separator />
    <q-card-section class='column items-center q-pa-md'>
      <div class="text-subtitle2"> Add Decision Options
        <q-btn
          color="grey"
          round
          flat
          dense
          :icon="optionsExpanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
          @click="optionsExpanded = !optionsExpanded" />
      </div>
      <q-slide-transition>
        <div v-show="optionsExpanded" style="width:100%">
          <div class="row items-center">
            <div class="col">
              <q-input filled class="q-my-md" style="width: 100%" v-model="newOption.title" label="Option Title" />
            </div>
            <q-btn round dense class="col-shrink q-mx-md" color="green-8" icon="add" @click="addDecisionOption()" />
          </div>
          <div class="column">
            <q-card v-for="(option,idx) in optionsList" :key="idx" class="row items-center q-mb-sm" style="width: 100%">
              <span class="col-1 text-h6 q-pl-sm">{{idx + 1}}.</span>
              <span class="col-10 text-body1">{{option.title}}</span>
              <q-btn color="negative" round dense icon="close" size="xs" @click="removeDecisionOption(option)" />
            </q-card>
          </div>
        </div>
      </q-slide-transition>
    </q-card-section>
    <q-card-section class="text-center text-body-1 text-negative" v-if="!submissionValid">
      A new decision requires a title, valid expiration date, and at least one option.
    </q-card-section>
    <q-card-actions align="right">
      <q-btn label="cancel" @click="onCancel()" />
      <q-btn color="green-8" @click="onCreate()" label="Create Decision" />
    </q-card-actions>
  </q-card>
</template>

<script>
import auth from 'src/store/auth'
export default {
  name: 'CreateDecisionModal',
  data () {
    return {
      currentUserName: '',
      newExpirationDate: '',
      newDecision: { ballots: [{}] },
      newOption: { title: '', userName: this.currentUserName },
      optionsList: [],
      allUsersList: [],
      filteredUsersList: [],
      addedUsers: [],
      newIncludedUser: '',
      detailsExpanded: true,
      usersExpanded: false,
      optionsExpanded: false,
      submissionValid: true,
      pickDatetimeDialog: false,
      ballotType: 1,
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

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.resetNewDecision()
    this.getAllUsers()
  },

  methods: {
    onCancel () {
      this.$emit('createClose')
    },

    async onCreate () {
      if (!this.checkValidSubmit()) {
        this.submissionValid = false
        return
      }

      this.newDecision.ballots[0].expirationDate = new Date(this.newExpirationDate).toISOString()
      this.addedUsers.forEach((user) => this.newDecision.includedUsers.push({ userName: user }))
      this.newDecision.ballots[0].ballotTypeId = this.ballotType
      this.newDecision.ballots[0].ballotOptions = this.optionsList

      try {
        await this.$axios.post(`${process.env.BACKEND_URL}/decision/`, this.newDecision)
        this.$emit('createClose')
      } catch (error) {
        console.log(error)
        this.$emit('error')
      }
    },

    async getAllUsers () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/users`)
        response.data.data.forEach((user) => this.allUsersList.push(user.userName))
      } catch (error) {
        console.log(error)
        this.$emit('error')
      }
    },

    resetNewDecision () {
      this.newDecision = {
        name: '',
        description: '',
        ballots: [{ expirationDate: '', ballotTypeId: 1, ballotOptions: [] }],
        ownerUsername: this.currentUserName,
        includedUsers: [
          { userName: this.currentUserName }
        ]
      }
      this.addedUsers = []
      this.expirationDate = ''
    },

    addIncludedUser () {
      if (this.newIncludedUser &&
          !this.addedUsers.includes(this.newIncludedUser) &&
          this.newIncludedUser !== this.currentUserName) {
        this.addedUsers.push(this.newIncludedUser)
      }
    },

    removeUser (user) {
      const pos = this.addedUsers.indexOf(user)
      this.addedUsers.splice(pos, 1)
    },

    addDecisionOption () {
      if (this.newOption.title !== '') {
        this.optionsList.push(this.newOption)
        this.newOption = { title: '', userName: this.currentUserName }
      }
    },

    removeDecisionOption (option) {
      const pos = this.optionsList.indexOf(option)
      this.optionsList.splice(pos, 1)
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

    checkValidDate (d) {
      const check = Date.parse(d)
      if (check) { return true }
      return false
    },

    checkValidSubmit () {
      if (this.newDecision.name === '') { return false }
      if (!this.checkValidDate(this.newExpirationDate)) { return false }
      if (!this.ballotType || this.ballotType === '') { return false }
      if (this.optionsList.length < 1) { return false }
      return true
    },

    closeDatetimeDialog () {
      this.pickDatetimeDialog = false
    },

    openDatetimeDialog () {
      this.pickDatetimeDialog = true
    }
  }
}
</script>

<style lang="scss">
.date-picker {
  min-width: 620px;
}

@media (max-width: $breakpoint-xs-max) {
  .date-picker {
    min-width: 375px;
  }
}
</style>
