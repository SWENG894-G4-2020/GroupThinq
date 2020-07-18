<template>
  <q-card style="width:50%; min-width:400px">
    <q-card-section class='column items-center q-pa-md'>
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
          <q-input filled class="q-my-md" style="width: 100%" v-model="newDecision.name" label="Title" />
          <q-input filled type="textarea" class="q-mb-md" style="width: 100%; max-height: 6em" v-model="newDecision.description" label="Description" />
          <q-input filled v-model="newDecision.ballots[0].expirationDate" label="Expiration Date" hint="YYYY/MM/DD HH:mm" style="width: 100%">
            <template v-slot:prepend>
              <q-icon name="event" class="cursor-pointer">
                <q-popup-proxy transition-show="scale" transition-hide="scale">
                  <q-date v-model="newDecision.ballots[0].expirationDate" mask="YYYY-MM-DD HH:mm" />
                </q-popup-proxy>
              </q-icon>
            </template>
            <template v-slot:append>
              <q-icon name="access_time" class="cursor-pointer">
                <q-popup-proxy transition-show="scale" transition-hide="scale">
                  <q-time v-model="newDecision.ballots[0].expirationDate" mask="YYYY-MM-DD HH:mm" />
                </q-popup-proxy>
              </q-icon>
            </template>
          </q-input>
        </div>
      </q-slide-transition>
    </q-card-section>
    <q-separator />
    <q-card-section class='column items-center q-pa-md'>
      <div class="text-subtitle2"> Add/Edit Members (by username)
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
              label="Add User(s)"
              filled fill-input use-input hide-selected
              v-model="newIncludedUser"
              input-debounce="0"
              :options="filteredUsersList" @filter="filterFn"
              class="q-my-md" style="width: 100%">
              <template v-slot:no-option>
                <q-item>
                  <q-item-section class="text-grey">
                    No results
                  </q-item-section>
                </q-item>
              </template>
            </q-select>
          </div>
          <q-btn round dense color="green-8" icon="add" class="col-shrink q-mx-md" @click="addIncludedUser()" />
        </div>
        <div class="row items-center" style="width: 100%">
          <q-chip v-for="(addedUser,idx) in addedUsers" :key="idx"
            removable
            :label="addedUser"
            @remove="removeUser(addedUser)"
            class ="col-shrink" />
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
              <q-input filled class="q-mb-md" style="width: 100%" v-model="newOption.description" label="Option Description" />
            </div>
            <q-btn round dense class="col-shrink q-mx-md" color="green-8" icon="add" @click="addDecisionOption()" />
          </div>
          <div class="column">
            <div v-for="(option,idx) in optionsList" :key="idx" class="row items-center" style="max-width:300px">
              <span class="text-h6 q-mr-md">{{idx + 1}}.</span>
              <span class="text-body1 q-mr-lg">{{option.title}}:  {{option.description}}</span>
              <q-btn color="grey" round dense icon="close" size="xs" @click="removeDecisionOption(option)" />
            </div>
          </div>
        </div>
      </q-slide-transition>
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
      newDecision: { ballots: [{}] },
      newOption: { title: '', description: '', userName: this.currentUserName },
      optionsList: [],
      allUsersList: [],
      filteredUsersList: [],
      addedUsers: [],
      newIncludedUser: '',
      detailsExpanded: true,
      usersExpanded: false,
      optionsExpanded: false
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
      this.newDecision.ballots[0].expirationDate = new Date(this.newDecision.ballots[0].expirationDate).toISOString()
      this.addedUsers.forEach((user) => this.newDecision.includedUsers.push({ userName: user }))

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
        ballots: [{ expirationDate: '', ballotOptions: [] }],
        ownerUsername: this.currentUserName,
        includedUsers: [
          { userName: this.currentUserName }
        ]
      }
      this.addedUsers = []
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
      if (this.newOption.title !== '' && this.newOption.description !== '') {
        this.optionsList.push(this.newOption)
        this.newOption = { title: '', description: '', userName: this.currentUserName }
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
        this.filteredUsersList = this.allUsersList.filter(v => v.toLowerCase().indexOf(needle) > -1)
      })
    }
  }
}
</script>
