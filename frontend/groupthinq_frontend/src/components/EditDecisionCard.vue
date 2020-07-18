<template>
  <q-card style="width:50%">
    <q-card-section class='column items-center q-pa-md'>
      <div class="text-h5 q-ma-md"> Editing Decision... </div>
      <div class="text-subtitle2"> Decision Details </div>
      <q-input filled class="q-mb-md" style="width: 100%" v-model="editableDecision.name" label="Title" />
      <q-input filled type="textarea" class="q-mb-md" style="width: 100%" v-model="editableDecision.description" label="Description" />
      <q-input filled v-model="editableDecision.ballots[0].expirationDate" label="Expiration Date" hint="YYYY/MM/DD HH:mm" style="width: 100%">
        <template v-slot:prepend>
          <q-icon name="event" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-date v-model="editableDecision.ballots[0].expirationDate" mask="YYYY-MM-DD HH:mm" />
            </q-popup-proxy>
          </q-icon>
        </template>
        <template v-slot:append>
          <q-icon name="access_time" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-time v-model="editableDecision.ballots[0].expirationDate" mask="YYYY-MM-DD HH:mm" />
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>
      <q-separator class="q-my-md" />
      <div class="text-subtitle2"> Add/Edit Members (by username) </div>
      <div class="row items-center" style="width: 100%">
        <div class="col">
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
      </q-select>
      </div>
      <div class="col-shrink q-px-sm">
      <q-btn round dense color="green-8" icon="add" @click="addIncludedUser()" />
      </div>
      </div>
      <div class="row items-center" style="width: 100%">
      <q-chip v-for="(includedUser,idx) in includedUsers" :key="idx"
        removable
        :label="includedUser.userName"
        @remove="removeUser(includedUser.userName)"
        class ="col-shrink" />
      </div>
      <q-separator class="q-my-md" />
    </q-card-section>
    <q-card-actions align="right">
      <q-btn label="cancel" @click="onCancel()" />
      <q-btn color="green-8" @click="onEditConfirm()" label="Confirm Edit" />
    </q-card-actions>
  </q-card>
</template>

<script>
import auth from 'src/store/auth'
export default {
  name: 'EditDecisionModal',
  data () {
    return {
      currentUserName: '',
      editableDecision: { ballots: [{}] },
      allUsersList: [],
      filteredUsersList: [],
      newIncludedUser: ''
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
    this.fillEditableDecision()
    this.getAllUsers()
  },

  methods: {
    onCancel () {
      this.$emit('editClose')
    },

    async onEditConfirm () {
      this.editableDecision.ballots[0].expirationDate = new Date(this.editableDecision.ballots[0].expirationDate).toISOString()
      console.log({ name: 'TestEdit', ownerUsername: this.currentUserName })
      try {
        await this.$axios.put(`${process.env.BACKEND_URL}/decision/${this.id}`, { name: 'TestEdit', ownerUsername: this.currentUserName, includedUsers: [{ userName: 'test' }] })
        this.$emit('editClose')
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

    fillEditableDecision () {
      this.editableDecision = {
        id: this.id,
        name: this.name,
        description: this.description,
        includedUsers: this.includedUsers,
        ballots: this.ballots
      }
    },

    addIncludedUser () {
      if (this.newIncludedUser &&
          !this.editableDecision.includedUsers.includes({ userName: this.newIncludedUser }) &&
          this.newIncludedUser !== this.currentUserName) {
        this.editableDecision.includedUsers.push({ userName: this.newIncludedUser })
      }
    },

    removeUser (user) {
      if (user !== this.currentUserName) {
        const pos = this.editableDecision.includedUsers.findIndex((element) => element.userName === user)
        this.editableDecision.includedUsers.splice(pos, 1)
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
