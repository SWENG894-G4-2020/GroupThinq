<template>
  <q-card style="width:50%">
    <q-card-section class='column items-center q-pa-md'>
      <div class="text-h5 q-ma-md"> Create A New Decision </div>
      <q-input filled class="q-mb-md" style="width: 100%" v-model="newDecision.name" label="Title" />
      <q-input filled type="textarea" class="q-mb-md" style="width: 100%" v-model="newDecision.description" label="Description" />
      <q-input filled v-model="newDecision.expirationDate" label="Expiration Date" hint="YYYY/MM/DD HH:mm" style="width: 100%">
        <template v-slot:prepend>
          <q-icon name="event" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-date v-model="newDecision.expirationDate" mask="YYYY-MM-DD HH:mm" />
            </q-popup-proxy>
          </q-icon>
        </template>

        <template v-slot:append>
          <q-icon name="access_time" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-time v-model="newDecision.expirationDate" mask="YYYY-MM-DD HH:mm" />
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>
      <q-select
        filled
        v-model="newIncludedUser"
        use-input
        hide-selected
        fill-input
        input-debounce="0"
        label="Add User(s)"
        :options="filteredUsersList"
        @filter="filterFn"
        class="q-my-md"
        style="width: 100%">
        <template v-slot:no-option>
          <q-item>
            <q-item-section class="text-grey">
              No results
            </q-item-section>
          </q-item>
        </template>
        <template v-slot:append>
          <q-btn round dense color="green-8" icon="add" @click="addIncludedUser()" />
        </template>
      </q-select>
      {{addedUsers}}
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
      newDecision: {},
      allUsersList: [],
      filteredUsersList: [],
      addedUsers: [],
      newIncludedUser: ''
    }
  },

  mounted () {
    this.resetNewDecision()
    this.getAllUsers()
  },

  methods: {
    onCancel () {
      this.$emit('createClose')
    },

    async onCreate () {
      this.newDecision.expirationDate = new Date(this.newDecision.expirationDate).toISOString()
      this.newDecision.includedUsers.push({ userName: 'foofoo' })
      try {
        await this.$axios.post(`${process.env.BACKEND_URL}/decision/`, this.newDecision)
        this.$emit('createClose')
      } catch (error) {
        console.log(error)
        this.$emit('error')
      }
    },

    async getAllUsers () {
      const response = await this.$axios.get(`${process.env.BACKEND_URL}/users`)
      response.data.data.forEach((user) => this.allUsersList.push(user.userName))
    },

    resetNewDecision () {
      const currentUserName = auth.getTokenData().sub
      this.newDecision = {
        name: '',
        description: '',
        expirationDate: '',
        ownerUsername: currentUserName,
        includedUsers: [
          { userName: currentUserName }
        ]
      }
      this.addedUsers = []
    },

    addIncludedUser () {
      if (this.newIncludedUser) {
        this.addedUsers.push(this.newIncludedUser)
      }
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
