<template>
  <q-card bordered style="height: 100%">
    <q-card-section>
      <div class="text-h5"><q-icon name="how_to_vote" /> Decision Details</div>
      <q-chip v-if="mode === 'view'" dense>
            <q-avatar icon="campaign" color="primary" text-color="white" />
            {{ decision.ownerUsername }}
      </q-chip>
      <q-input
      :readonly="(mode === 'create' || mode === 'edit') ? false : true"
      name="details-name"
      v-model="name"
      label="Name"
      hint="Short description of what must be decided."
      :rules="[val => !!val || '*Required', val => val.length < 161 || 'Decision name must be less than 160 characters']"
      />
      <q-input
      :readonly="(mode === 'create' || mode === 'edit') ? false : true"
      name="details-description"
      v-model="description"
      label='Additional Details'
      hint='(Optional)'
      :rules="[val => val.length < 999 || 'Description name must be less than 999 characters']"
      />
    </q-card-section>
    <q-card-section>
      <div class="text-h5"><q-icon name="person" /> Participants</div>
      <q-select
        label="Select Participants"
        name="details-users"
        hint="Start typing to search for users."
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
</template>

<script>
import auth from 'src/store/auth'

export default {
  name: 'DecisionDetailsCard',

  data () {
    return {
      currentUserName: '',
      name: '',
      description: '',
      allUsersList: [],
      filteredUsersList: [],
      selectedUsers: []
    }
  },

  props: {
    decision: {
      type: Object,
      required: false
    },

    mode: {
      type: String,
      required: true
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.getAllUsers()

    this.resetForm()

    if (this.decision) {
      this.populateWithDecision()
    }
  },

  methods: {
    async getAllUsers () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/users`)
        response.data.data.forEach((user) => this.allUsersList.push(user.userName))
      } catch (error) {
        console.log(error)
        this.$emit('error')
      }
    },

    resetForm () {
      this.name = ''
      this.description = ''
      this.selectedUsers = []
    },

    populateWithDecision () {
      this.name = this.decision.name
      if (this.decision.description) {
        this.description = this.decision.description
      }
      this.decision.includedUsers.forEach(user => this.selectedUsers.push(user.userName))
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

    isValid () {
      if (this.name === '' || this.name.length > 160) { return false }
      return true
    },

    getRequestObject () {
      const decision = {
        ballots: [],
        includedUsers: [{ userName: this.currentUserName }],
        name: this.name,
        description: this.description,
        ownerUsername: this.currentUserName
      }
      this.selectedUsers.forEach(user => decision.includedUsers.push({ userName: user }))
      return decision
    }
  }
}
</script>
