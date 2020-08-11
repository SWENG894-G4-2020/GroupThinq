<template>
  <q-card bordered style="height: 100%">
    <q-card-section class="q-pb-sm">
      <div class="text-h5 q-py-md"><q-icon name="ballot" color="grey-7"/> Ballot</div>
      <ExpirationField v-bind:mode="mode" v-bind:showTimer="showTimer" v-bind:initialDate="initialDate" ref="datetime" @expired="setExpired()"/>
      <div class="q-mt-sm">
        <div class="text-grey-8" style="font-size: 16px"> Voting Method</div>
        <q-btn-toggle
          name="ballot-type"
          v-if="mode === 'create'"
          spread
          v-model="ballotTypeId"
          toggle-color="primary"
          :options="ballotTypeOptions"
        />
        <div v-else class="q-pt-sm">{{ ballotTypeOptions.find(bt => bt.value === ballotTypeId ).label }}</div>
        <div class="text-grey-7">{{ ballotTypeOptions.find(bt => bt.value === ballotTypeId ).description }}</div>
      </div>
    </q-card-section>
    <q-card-section class="q-pt-none">
      <div class="text-h5 q-pb-sm"><q-icon name="list" class="text-grey-7" /> Ballot choices</div>
      <q-input v-if="!expired || mode === 'create'" class="q-mb-md" v-model="newOption.title" label="Add Choice" name="ballot-option-name">
        <template v-if="mode === 'create' || userIncluded" v-slot:append>
          <q-btn dense color="positive" icon="add" @click="addDecisionOption()" name="ballot-option-add"/>
        </template>
      </q-input>
      <div v-if="voting && ballotTypeId === 1" class="column">
        <q-option-group
          v-model="newVoteSelection"
          :options="newVote"
          color="primary"
        />
      </div>
      <div v-else-if="voting && ballotTypeId === 2" class="column">
        <div v-for="(option,idx) in newVote" :key="idx" class="row items-center q-mb-sm">
          <q-avatar color="primary" size="sm" class="q-mr-md text-white text-bold">{{option.rank}}</q-avatar>
          <span class="text-body1 col-grow">{{option.title}}</span>
          <q-btn flat color="positive" icon="expand_less" @click="moveRankUp(option)" name="ballot-option-rankup"/>
          <q-btn flat color="negative" icon="expand_more" @click="moveRankDown(option)" name="ballot-option-rankdown"/>
        </div>
      </div>
      <div v-else class="column">
        <div v-for="(option,idx) in options" :key="idx" class="row items-center q-mb-sm">
          <q-icon v-if="isMyVote(option) && ballotTypeId === 1" name="check" color="positive" class="q-pr-sm"/>
          <q-avatar v-else-if="isMyVote(option) && ballotTypeId === 2" color="primary" size="sm" class="q-mr-md text-white text-bold">{{option.rank}}</q-avatar>
          <q-icon v-else name="close" color="white" class="q-pr-sm"/>
          <span class="text-body1 col-grow">{{option.title}}</span>
          <q-btn v-if="mode === 'create'" flat color="negative" icon="close" @click="removeDecisionOption(option)" name="ballot-option-delete"/>
        </div>
      </div>
      <div class="row reverse q-gutter-sm">
        <q-btn v-if="!expired && mode === 'view' && !voting" icon="ballot" color="primary" :label="haveVoted ? 'Change Vote' : 'Vote'" name="ballot-vote-start" @click="onVoteStart()"/>
        <q-btn v-if="!expired && mode === 'view' && voting" icon="check" color="positive" label="Confirm" name="ballot-vote-confirm" @click="onVoteConfirm()"/>
        <q-btn v-if="!expired && mode === 'view' && voting" icon="close" label="Cancel" name="ballot-vote-cancel" @click="onVoteCancel()"/>
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
import auth from 'src/store/auth'
import ExpirationField from 'src/components/ExpirationField'

export default {
  name: 'BallotCard',

  components: {
    ExpirationField
  },

  data () {
    return {
      currentUserName: '',
      ballotTypeId: 1,
      ballotTypeOptions: [
        {
          label: 'First Past the Post',
          value: 1,
          description: 'The choice with the most votes win. Voters pick one choice.'
        },
        {
          label: 'Ranked Pair',
          value: 2,
          description: 'Selects a single winner using votes that express preferences. Voters rank the choices.'
        }
      ],
      newOption: { title: '', userName: this.currentUserName },
      options: [],
      newVote: [],
      newVoteSelection: -1,
      expired: false,
      initialDate: '',
      votes: [],
      voteMode: 'view',
      voting: false
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
    this.newOption.userName = this.currentUserName

    this.resetForm()

    if (this.decision) {
      this.populateWithBallot(this.decision.ballots[0])
    }
  },

  computed: {
    myVotes: function () {
      return this.votes.filter(vote => vote.userName === this.currentUserName)
    },

    haveVoted: function () {
      return this.myVotes.length > 0
    },

    showTimer: function () {
      if (this.mode === 'view') {
        return true
      }
      return false
    },

    userIncluded: function () {
      if (this.decision && this.decision.includedUsers.find(user => user.userName === this.currentUserName)) {
        return true
      }
      return false
    }
  },

  methods: {
    resetForm () {
      this.initialDate = ''
      this.ballotTypeId = 1
      this.options = []
      this.votes = []
      this.newOption = { title: '', userName: this.currentUserName }
      this.newVoteSelection = -1
      this.newVote = []
    },

    createNewVote () {
      const vote = []
      let iter = 1
      this.options.forEach(option => {
        const opt = option
        const myVote = this.myVotes.find(v => v.ballotOptionId === option.id)
        if (myVote) {
          opt.vote = true
          opt.rank = myVote.rank
          opt.voteId = myVote.id
        } else {
          opt.vote = false
          opt.rank = iter
        }
        opt.label = option.title
        opt.value = option.id
        vote.push(opt)
        iter++
      })
      vote.sort((a, b) => a.rank - b.rank)
      return vote
    },

    async populateWithBallot (ballot) {
      this.initialDate = ballot.expirationDate
      this.ballotTypeId = ballot.ballotTypeId
      this.options = []
      ballot.ballotOptions.forEach(option => this.options.push(option))
      await this.getVotes()
      this.myVotes.forEach(vote => {
        const opt = this.options.find(opt => vote.ballotOptionId === opt.id)
        opt.rank = vote.rank
      })
      this.options.sort((a, b) => a.rank - b.rank)
    },

    async addDecisionOption () {
      if (this.mode === 'create' && this.newOption.title !== '') {
        this.options.push({ title: this.newOption.title, userName: this.currentUserName })
      } else if (this.newOption.title !== '') {
        await this.addBallotOption({ title: this.newOption.title, userName: this.currentUserName, ballotId: this.decision.ballots[0].id })
        this.$emit('reload')
      }
      this.newOption = { title: '', userName: this.currentUserName }
    },

    removeDecisionOption (option) {
      const pos = this.options.indexOf(option)
      this.options.splice(pos, 1)
    },

    getExpiration () {
      return this.$refs.datetime.datetime
    },

    setExpired () {
      this.$emit('expired')
      this.expired = true
    },

    isMyVote (option) {
      return this.myVotes.find(v => v.ballotOptionId === option.id)
    },

    isValid () {
      if (!this.$refs.datetime.isValid()) { return false }
      if (!this.ballotTypeId || this.ballotTypeId === '' || this.ballotTypeId < 1 || this.ballotTypeId > 2) { return false }
      if (this.options < 1) { return false }
      return true
    },

    getRequestObject () {
      const ballot = {
        ballotTypeId: this.ballotTypeId,
        ballotOptions: [],
        expirationDate: new Date(this.getExpiration())
      }

      if (this.decision) {
        ballot.id = this.decision.ballots[0].id
        this.options.forEach(option => ballot.ballotOptions.push({
          title: option.title,
          userName: this.currentUserName,
          ballotId: this.decision.ballots[0].id,
          id: option.id
        }))
      } else {
        this.options.forEach(option => ballot.ballotOptions.push({ title: option.title, userName: this.currentUserName }))
      }

      return ballot
    },

    moveRankUp (option) {
      if (option.rank > 1) {
        const swap = this.newVote.find(opt => opt.rank === (option.rank - 1))
        swap.rank = option.rank
        option.rank = option.rank - 1
      } else {
        this.newVote.forEach(opt => opt.rank--)
        option.rank = this.newVote.length
      }
      this.newVote.sort((a, b) => a.rank - b.rank)
    },

    moveRankDown (option) {
      if (option.rank < this.newVote.length) {
        const swap = this.newVote.find(opt => opt.rank === (option.rank + 1))
        swap.rank = option.rank
        option.rank = option.rank + 1
      } else {
        this.newVote.forEach(opt => opt.rank++)
        option.rank = 1
      }
      this.newVote.sort((a, b) => a.rank - b.rank)
    },

    async addBallotOption (option) {
      try {
        await this.$axios.post(`${process.env.BACKEND_URL}/ballot/${this.decision.ballots[0].id}/option`, option)
        this.getBallot()
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    },

    async getBallot () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/decision/${this.decision.id}`)
        await this.getVotes()
        this.populateWithBallot(response.data.data[0].ballots[0])
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    },

    async getVotes () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/ballot/${this.decision.ballots[0].id}/votes`)
        this.votes = response.data.data
        if (this.haveVoted && this.ballotTypeId === 1) {
          this.newVoteSelection = this.myVotes[0].ballotOptionId
        }
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    },

    onVoteStart () {
      this.voting = true
      this.newVote = this.createNewVote()
    },

    async onVoteConfirm () {
      const votePayload = []
      if (this.ballotTypeId === 1) {
        if (this.newVoteSelection < 0) {
          this.onVoteCancel()
          return
        }
        const newVote = {
          ballotId: this.decision.ballots[0].id,
          ballotOptionId: this.newVoteSelection,
          userName: this.currentUserName
        }
        if (this.haveVoted) {
          newVote.id = this.myVotes[0].id
        }

        votePayload.push(newVote)
      } else {
        this.newVote.forEach(vote => {
          votePayload.push({
            ballotId: this.decision.ballots[0].id,
            ballotOptionId: vote.id,
            id: vote.voteId,
            userName: this.currentUserName,
            rank: vote.rank
          })
        })
      }

      try {
        if (this.haveVoted) {
          await this.$axios.put(`${process.env.BACKEND_URL}/ballot/${this.decision.ballots[0].id}/vote`, votePayload)
        } else {
          await this.$axios.post(`${process.env.BACKEND_URL}/ballot/${this.decision.ballots[0].id}/vote`, votePayload)
        }
        await this.getVotes()
        this.populateWithBallot(this.decision.ballots[0])
        this.onVoteCancel()
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    },

    onVoteCancel () {
      this.voting = false
      this.newVote = []
    }
  }
}
</script>
