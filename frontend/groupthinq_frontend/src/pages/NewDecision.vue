<template>
  <q-page class="q-pa-md full-width" >
    <div class="row">
      <div class="q-pa-sm col-xs-12 col-md-6">
        <DecisionDetailsCard ref="details" v-bind:mode="mode" />
      </div>
    </div>
    <div class="q-pa-sm col-xs-12">
      <q-banner v-if="!submissionValid" class="bg-red-1 q-my-sm">
        <template v-slot:avatar>
          <q-icon name="warning" color="negative" />
        </template>
        A new decision requires a title, valid expiration date, and at least one option.
      </q-banner>
      <div class="row q-gutter-sm">
        <q-btn icon="add" color="positive" label="Create" @click="onCreate()" :loading="submitting" :disabled="submitting">
          <template v-slot:loading>
            <q-spinner />
          </template>
        </q-btn>
        <q-btn icon="clear" label="Cancel" to="/main" />
      </div>
    </div>
  </q-page>
</template>

<script>
import DecisionDetailsCard from 'src/components/DecisionDetailsCard'

export default {
  name: 'PageDecisions',

  components: {
    DecisionDetailsCard
  },

  data () {
    return {
      mode: 'create',
      submissionValid: true
    }
  },

  computed: {

  },

  mounted () {

  },

  methods: {
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
