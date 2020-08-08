<template>
<div class="q-pa-sm">
  <q-card bordered class="column" style="height: 100%">
    <q-card-section class="q-py-xs">
      <div class="text-h5">
        <q-icon v-if="!expired" name="event_available" class="text-positive"  />
        <q-icon v-else name="event_busy" class="text-negative"/>
        {{ decision.name }}
      </div>
      <div class="text-caption">
        <span v-if="!expired" class="text-positive">&nbsp;Voting Open</span>
        <span v-else class="text-negative">&nbsp;Voting Closed</span>
      </div>
    </q-card-section>
    <q-card-section class="q-py-xs">
      <div class="row">
        <div v-for="(user) in truncatedUsers" :key="user.id">
          <q-chip dense>
            <q-avatar v-if="user.userName === decision.ownerUsername" icon="campaign" color="primary" text-color="white" />
            <q-avatar v-else icon="person" />
            {{ user.userName }}
          </q-chip>
        </div>
        <div v-if="overUsers" class="q-py-xs text-grey-7">... + {{ overUsers }} more</div>
      </div>
    </q-card-section>
    <q-card-section class="q-py-xs col-grow">
      <div v-if="!expired"><q-icon name="alarm_on" class="text-positive" /> {{daysRemaining}}d {{hoursRemaining}}h {{minutesRemaining}}m {{secondsRemaining}}s</div>
      <div><q-icon v-if="expired" name="alarm_off" class="text-negative" /> {{ prettyDate }}</div>
    </q-card-section>
    <q-card-actions vertical >
      <q-btn
        v-if="!expired"
        icon="how_to_vote"
        label="Vote"
        class="q-mx-xs"
        color="primary"
        :to="'/decisions/' + decision.id"
         />
      <q-btn
        v-else
        icon="poll"
        label="View Results"
        class="q-mx-xs"
        color="primary"
        :to="'/decisions/' + decision.id"
         />
    </q-card-actions>
  </q-card>
</div>
</template>

<script>
import auth from 'src/store/auth'
import { date } from 'quasar'

export default {
  name: 'DecisionSummaryCard',

  data () {
    return {
      currentUserName: '',
      expirationDate: '',
      daysRemaining: '',
      hoursRemaining: '',
      minutesRemaining: '',
      secondsRemaining: ''
    }
  },

  props: {
    decision: {
      type: Object,
      required: true
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.calculateRemainingTime()
  },

  computed: {
    expired: function () {
      const diff = (new Date(this.decision.ballots[0].expirationDate) - Date.now()) / 1000
      if (diff < 0) { return true }
      return false
    },

    isOpen: function () {
      if (!this.expired) {
        return true
      } else {
        return false
      }
    },

    prettyDate: function () {
      return date.formatDate(new Date(this.decision.ballots[0].expirationDate), 'YYYY/MM/DD HH:mm')
    },

    overUsers: function () {
      if (this.decision.includedUsers.length > 9) {
        return this.decision.includedUsers.length - 9
      } else {
        return 0
      }
    },

    truncatedUsers: function () {
      if (this.decision.includedUsers.length > 9) {
        return this.decision.includedUsers.slice(0, 9)
      } else {
        return this.decision.includedUsers
      }
    }
  },

  methods: {
    calculateRemainingTime () {
      const secondsTimer = setInterval(() => {
        const diff = (new Date(this.decision.ballots[0].expirationDate) - Date.now()) / 1000

        if (diff < 0) {
          this.$emit('reload')
          clearInterval(secondsTimer)
          return
        }

        const days = Math.floor(diff / (3600 * 24))
        const hours = Math.floor((diff % (3600 * 24)) / 3600)
        const minutes = Math.floor((diff % 3600) / 60)
        const seconds = Math.floor((diff % 60))
        this.daysRemaining = days
        this.hoursRemaining = hours
        this.minutesRemaining = minutes
        this.secondsRemaining = seconds
      }, 500)
    }
  }
}
</script>
