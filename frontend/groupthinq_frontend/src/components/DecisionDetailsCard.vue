<template>
  <q-card bordered style="height: 100%">
    <q-card-section>
      <q-input
      :readonly="(mode === 'create' || mode === 'edit') ? false : true"
      name="details-name"
      autogrow
      v-model="name"
      label="Decision"
      class="text-h5"
      :rules="[val => !!val || '*Required', val => val.length < 161 || 'Decision name must be less than 160 characters']"
      >
        <template v-slot:prepend>
          <q-icon name="how_to_vote" />
        </template>
      </q-input>
      <div v-if="mode !== 'create' ">
        <span class="text-grey-7">A decision by</span>
        <q-chip v-if="mode !== 'create' " dense>
              <q-avatar icon="campaign" color="primary" text-color="white" />
              {{ decision ? decision.ownerUsername : '' }}
        </q-chip>
      </div>
      <q-input
      :readonly="(mode === 'create' || mode === 'edit') ? false : true"
      name="details-description"
      v-if="(mode === 'create' || mode === 'edit') || description"
      v-model="description"
      label='Additional Details'
      hint='(Optional)'
      :rules="[val => val.length < 999 || 'Description name must be less than 999 characters']"
      />
    </q-card-section>
    <q-card-section>
      <div class="text-h5"><q-icon name="person" class="text-grey-7" /> Participants</div>
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

  watch: {
    mode: function (newMode, oldMode) {
      if (newMode === 'edit') {
        this.selectedUsers = this.selectedUsers.filter(v => v !== this.currentUserName)
      }
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

      if (this.decision) {
        decision.id = this.decision.id
      }
      return decision
    }
  }
}
</script>
