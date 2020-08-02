<template>
  <div>
    <q-input
    v-model="datetime"
    name="expiration-datetime"
    label="Voting end date"
    :rules="[val => checkValidDate(val) || '*Valid Date Required']"
    :readonly="(mode === 'create' || mode === 'edit') ? false : true"
    mask="datetime"
    hint="YYYY/MM/DD HH:mm">
      <template v-if="(mode === 'create' || mode === 'edit')" v-slot:append>
        <q-icon name="event" class="cursor-pointer" @click="openDatetimeDialog()">
          <q-dialog v-model="pickDatetimeDialog">
              <q-card class="date-picker">
                <q-card-section>
                  <div class="q-gutter-sm row justify-center">
                    <q-date today-btn v-model="datetime" mask="YYYY/MM/DD HH:mm" default-year-month="2020/08" />
                    <q-time now-btn v-model="datetime" mask="YYYY/MM/DD HH:mm" />
                  </div>
                </q-card-section>
                <q-card-actions align="right">
                  <q-btn color="positive" @click="closeDatetimeDialog()" label="Confirm" />
                </q-card-actions>
              </q-card>
          </q-dialog>
        </q-icon>
      </template>
    </q-input>
    <div v-if="showTimer && !expired"><q-icon name="alarm_on" class="text-positive" /> {{timer.daysRemaining}}d {{timer.hoursRemaining}}h {{timer.minutesRemaining}}m {{timer.secondsRemaining}}s remaining</div>
    <div v-if="showTimer && expired"><q-icon name="alarm_off" class="text-negative" /> Expired on {{prettyDate}}</div>
  </div>
</template>

<script>

export default {
  name: 'ExpirationField',

  data () {
    return {
      datetime: '',
      pickDatetimeDialog: false,
      timer: {
        daysRemaining: '',
        hoursRemaining: '',
        minutesRemaining: '',
        secondsRemaining: ''
      }
    }
  },

  props: {
    initialDate: {
      type: String,
      required: false
    },

    showTimer: {
      type: Boolean,
      required: false
    },

    mode: {
      type: String,
      required: true
    }
  },

  mounted () {
    if (this.initialDate) {
      this.initalizeDate()
    }
    this.calculateRemainingTime()
  },

  computed: {
    expired: function () {
      const diff = (new Date(this.datetime) - Date.now()) / 1000
      if (diff < 0) { return true }
      return false
    },

    prettyDate: function () {
      return new Date(this.datetime).toLocaleString()
    }
  },

  methods: {
    initalizeDate () {
      const date = new Date(this.initalizeDate)
      let month = date.getMonth + 1
      if (month < 10) { month = '0' + month }

      let day = date.getDate
      if (day < 10) { day = '0' + day }

      let hours = date.getHours
      if (hours < 10) { hours = '0' + hours }

      let minutes = date.getMinutes
      if (minutes < 10) { minutes = '0' + minutes }

      this.datetime = `${date.getFullYear}/${month}/${day} ${hours}:${minutes}`
    },

    isExpired () {
      return this.expired
    },

    checkValidDate (d) {
      const check = Date.parse(d)
      if (check) { return true }
      return false
    },

    isValid () {
      return this.checkValidDate(this.datetime)
    },

    closeDatetimeDialog () {
      this.pickDatetimeDialog = false
    },

    openDatetimeDialog () {
      this.pickDatetimeDialog = true
    },

    calculateRemainingTime () {
      const secondsTimer = setInterval(() => {
        const diff = (new Date(this.datetime) - Date.now()) / 1000

        if (diff < 0) {
          this.$forceUpdate()
          this.$emit('expired')
          clearInterval(secondsTimer)
          return
        }

        const days = Math.floor(diff / (3600 * 24))
        const hours = Math.floor((diff % (3600 * 24)) / 3600)
        const minutes = Math.floor((diff % 3600) / 60)
        const seconds = Math.floor((diff % 60))
        this.timer.daysRemaining = days
        this.timer.hoursRemaining = hours
        this.timer.minutesRemaining = minutes
        this.timer.secondsRemaining = seconds
      }, 500)
    }
  }
}
</script>

<style lang="scss" scoped>
.q-field--readonly .q-field__control:before {
    border-bottom-style: none !important;
}

.date-picker {
  min-width: 620px;
}

@media (max-width: $breakpoint-xs-max) {
  .date-picker {
    min-width: 375px;
  }
}
</style>
