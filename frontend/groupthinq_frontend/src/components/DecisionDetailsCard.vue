<template>
  <q-card bordered style="height: 100%">
    <q-card-section>
      <div class="text-h5"><q-icon name="how_to_vote" /> Decision Details</div>
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
      autogrow
      dense
      clearable
      class="text-body2 text-grey-5"
      placeholder="No Details"
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
      this.populateWithDecision(this.decision)
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

    populateWithDecision (decision) {
      this.name = this.decision.name
      this.description = this.decision.description
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
      if (this.decision.name === '' || this.decision.length > 160) { return false }
      return true
    }
  }
}
</script>

<style scoped>
.q-field--readonly .q-field__control:before {
    border-bottom-style: none !important;
}
</style>
