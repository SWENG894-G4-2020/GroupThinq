<template>
  <q-card>
    <q-card-section class="q-pa-md column items-center">
    <div class="q-px-md q-mt-md text-h5">Vote for {{title}}</div>
    <div class="text-subtitle1">{{description}}</div>
    <q-option-group
      class="q-my-md"
      :options="formattedVoteOptions"
      label="vote"
      type="radio"
      v-model="voteResult" />
      <div class="q-pa-md text-h6">Current Selection: {{voteResult}}</div>
    </q-card-section>
    <q-card-actions align="right">
      <q-btn label="cancel" @click="$emit('votingClose')" />
      <q-btn color="green-8" @click="onSubmit()" label="Submit Vote" />
    </q-card-actions>
    <!-- <q-btn label="Close" @click="$emit('votingClose')" /> -->
  </q-card>
</template>

<script>
export default {
  name: 'VotingCard',
  data () {
    return {
      voteResult: ''
    }
  },

  computed: {
    formattedVoteOptions: function () {
      return this.ballotOptions.map(option => ({ label: `${option.title}: ${option.description}`, value: option.title }))
    }

  },

  props: {
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
    onSubmit () {
      // TODO: Connect to the backend API for vote submission
    }

  }
}
</script>
