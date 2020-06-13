<template>
  <q-card bordered class="q-mx-xl q-my-md">
    <q-card-section>
      <div class="text-h5 q-mt-sm q-mb-xs row justify-between">
        <div class="col">{{title}}</div>
      </div>
      <div class="text-caption text-grey">{{description}}</div>
      <div class="text-negative" v-if="!expired">Remaining: {{daysRemaining}}d {{hoursRemaining}}h {{minutesRemaining}}m {{secondsRemaining}}s</div>
      <div class="text-negative" v-else>Voting has closed.</div>
    </q-card-section>
    <q-card-actions class="row justify-between">
      <div class="col">
        <q-btn
            color="grey"
            round
            flat
            dense
            :icon="expanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
            @click="expanded = !expanded"
        />
        DETAILS
      </div>
      <div class="justify-end">
          <q-btn label="View Results"
            class="q-mx-xs"
            :outline="!status.results"
            :disable="!status.results"
            color="primary" />
          <q-btn label="Nominate"
            class="q-mx-xs"
            :outline="!status.nominate"
            :disable="!status.nominate"
            color="primary" />
          <q-btn label="Vote"
            class="q-mx-xs"
            :outline="!status.vote"
            :disable="!status.vote"
            color="primary" />
        </div>
    </q-card-actions>

    <q-slide-transition>
      <div v-show="expanded">
        <q-separator />
        <q-card-section horizontal>
            <q-card-section class="col-4 q-mx-md">
                <div class="text-overline">Owner</div>
                <div class="text-h7">{{ owner }}</div>
            <q-separator class="q-my-md" />
                <div class="text-overline">Members</div>
                <div class="text-h7 row"> not implemented. </div>
                <!-- <div class="text-h7 row" v-for="user in users" :key="user">{{ user }}</div> -->
            </q-card-section>
            <q-card-section>
                <div class="text-overline">Voting Deadline</div>
                <div class="text-h7 row">{{ expiration }}</div>
                <q-separator class="q-my-md" />
                <div class="text-overline">Nomination Deadline</div>
                <div class="text-h7 row"> not implemented. </div>
            </q-card-section>
        </q-card-section>
      </div>
    </q-slide-transition>
  </q-card>
</template>

<script>
export default {
  name: 'DecisionCard',

  data () {
    return {
      expanded: false,
      expired: false,
      daysRemaining: '',
      hoursRemaining: '',
      minutesRemaining: '',
      secondsRemaining: ''
    }
  },

  computed: {
    status: function () {
      if (!this.expired) {
        return { vote: true, nominate: false, results: false }
      } else {
        return { vote: false, nominate: false, results: true }
      }
    }
  },

  props: {
    title: {
      type: String,
      required: true
    },

    description: {
      type: String,
      default: ''
    },

    expiration: {
      type: Date,
      required: true
    },

    owner: {
      type: String,
      default: ''
    },

    users: {
      type: Array,
      default: function () {
        return []
      }
    }
  },

  mounted () {
    this.calculateRemainingTime()
  },

  methods: {
    calculateRemainingTime () {
      const secondsTiemr = setInterval(() => {
        const diff = (this.expiration - Date.now()) / 1000

        if (diff < 0) {
          clearInterval(secondsTiemr)
          this.expired = true
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
      }, 1000)
    }
  }
}
</script>
