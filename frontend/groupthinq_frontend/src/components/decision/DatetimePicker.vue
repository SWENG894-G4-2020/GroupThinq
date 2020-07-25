<template>
  <q-input v-model="datetime" label="Expiration Date" :rules="[val => checkValidDate(val) || '*Valid Date Required']" mask="datetime" style="width: 100%" hint="YYYY/MM/DD HH:mm">
    <template v-slot:append>
      <q-icon name="event" class="cursor-pointer" @click="openDatetimeDialog()">
        <q-dialog v-model="pickerDialog">
            <q-card class="date-picker">
              <q-card-section>
                <div class="q-gutter-sm row justify-center">
                  <q-date today-btn v-model="datetime" mask="YYYY/MM/DD HH:mm" default-year-month="2020/08" />
                  <q-time now-btn v-model="datetime" mask="YYYY/MM/DD HH:mm" />
                </div>
              </q-card-section>
              <q-card-actions align="right">
                <q-btn color="green-8" @click="closeDatetimeDialog()" label="Confirm" />
              </q-card-actions>
            </q-card>
        </q-dialog>
      </q-icon>
    </template>
  </q-input>
</template>

<script>
export default {
  name: 'DatetimePicker',

  data () {
    return {
      pickerDialog: false,
      datetime: this.initialDatetime
    }
  },

  props: {
    initialDatetime: {
      type: String,
      required: true
    }
  },

  computed: {
    pretty: function () {
      return new Date(this.datetime).toISOString()
    }
  },

  methods: {
    checkValidDate (d) {
      const check = Date.parse(d)
      if (check) { return true }
      return false
    },

    closeDatetimeDialog () {
      this.pickerDialog = false
    },

    openDatetimeDialog () {
      this.pickerDialog = true
    }
  }
}
</script>

<style lang="scss">
.date-picker {
  min-width: 620px;
}

@media (max-width: $breakpoint-xs-max) {
  .date-picker {
    min-width: 375px;
  }
}
</style>
