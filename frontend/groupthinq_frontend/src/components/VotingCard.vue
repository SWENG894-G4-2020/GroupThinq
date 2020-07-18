<template>
  <q-card style="width=100%">
    <q-card-section class="q-pa-md column items-center" v-if="!previousVote">
    <div class="q-px-md q-mt-md text-h5">Vote for {{title}}</div>
    <div class="text-subtitle1">{{description}}</div>
    <q-option-group
      class="q-my-md"
      :options="formattedVoteOptions"
      label="vote"
      type="radio"
      v-model="voteResult" />
      <div class="q-pa-md text-h6">Current Selection: {{voteResult}}</div>
      <div class="text-body1 text-negative" v-if="selectionError">You must make a selection to vote.</div>
    </q-card-section>
    <q-card-section class="q-pa-md column items-center" v-else>
      <div class="q-px-md q-mt-md text-h5">Your vote was:</div>
      <div class="q-px-md q-mt-sm text-h6">{{previousVote.title}}</div>
      <div class="q-px-md q-mt-sm text-body-1">{{previousVote.description}}</div>
    </q-card-section>
    <q-card-actions align="right">
      <q-btn label="cancel" @click="$emit('votingClose')" />
      <q-btn color="green-8" :disable="!!previousVote" @click="onSubmit()" :label="!previousVote ? 'Submit Vote' : 'Vote Submitted'" />
    </q-card-actions>
  </q-card>
</template>

<script>
import auth from 'src/store/auth'
export default {
  name: 'VotingCard',
  data () {
    return {
      voteResult: '',
      selectionError: false
    }
  },

  computed: {
    formattedVoteOptions: function () {
      return this.ballotOptions.map(option => ({ label: `${option.title}: ${option.description}`, value: option.title }))
    },

    voteResultID: function () {
      let option
      for (option of this.ballotOptions) {
        if (option.title === this.voteResult) {
          return option.id
        }
      }
      return -1
    }
  },

  props: {
    previousVote: {
      type: Object
    },

    ballotId: {
      type: Number,
      required: true
    },

    title: {
      type: String,
      required: true
    },

    description: {
      type: String
    },

    ballotOptions: {
      type: Array,
      required: true
    }
  },

  methods: {
    async onSubmit () {
      if (this.voteResultID === -1) {
        this.selectionError = true
        return
      }
      try {
        const userName = auth.getTokenData().sub
        const vote = { ballotId: this.ballotId, ballotOptionId: this.voteResultID, userName: userName }
        await this.$axios.post(`${process.env.BACKEND_URL}/ballot/${this.ballotId}/vote`, vote)
      } catch (error) {
        console.log(error)
        this.isError = true
      }
      this.$emit('votingClose')
    }

  }
}
</script>
