<template>
  <q-card style="width:50%; min-width:400px">
    <q-card-section class="q-pa-md column items-center" v-if="!previousVote">
    <div class="text-overline">Submit a vote for:</div>
    <div class="text-h5">{{title}}</div>
    <div class="text-subtitle1 q-mb-md">{{description}}</div>
    <q-separator />
    <div class="text-overline">Voting Options:</div>
    <q-option-group
      class=""
      :options="formattedVoteOptions"
      label="vote"
      type="radio"
      v-model="voteResult" />
    <q-separator />
    <div class="q-pa-md">
      <span class="text-h6 q-pr-sm text-weight-light">Current Selection:</span>
      <span class="text-h6">{{voteResult}}</span>
    </div>
      <div class="text-body1 text-negative" v-if="selectionError">You must make a selection to vote.</div>
    </q-card-section>
    <q-card-section class="q-pa-md column items-center" v-else>
      <div class="text-overline">Your submitted vote was:</div>
      <div class="q-px-md q-mt-sm text-h6">{{previousVote.title}}</div>
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
      return this.ballotOptions.map(option => ({ label: `${option.title}`, value: option.title }))
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
