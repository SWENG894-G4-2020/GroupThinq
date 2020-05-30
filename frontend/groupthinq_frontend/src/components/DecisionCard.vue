<template>
  <q-card bordered class="q-ma-xl">
    <q-card-section>
      <div class="text-h5 q-mt-sm q-mb-xs row justify-between">
        <div class="col">{{Title}}</div>
      </div>
      <div class="text-caption text-grey">{{Description}}</div>
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
            :outline="!Status.results"
            :disable="!Status.results"
            color="primary" />
          <q-btn label="Nominate"
            class="q-mx-xs"
            :outline="!Status.nominate"
            :disable="!Status.nominate"
            color="primary" />
          <q-btn label="Vote"
            class="q-mx-xs"
            :outline="!Status.vote"
            :disable="!Status.vote"
            color="primary" />
        </div>
    </q-card-actions>

    <q-slide-transition>
      <div v-show="expanded">
        <q-separator />
        <q-card-section horizontal>
            <q-card-section class="col-4 q-mx-md">
                <div class="text-overline">Owner</div>
                <div class="text-h7">{{ Owner }}</div>
            <q-separator class="q-my-md" />
                <div class="text-overline">Members</div>
                <div class="text-h7 row" v-for="user in Users" :key="user">{{ user }}</div>
            </q-card-section>
            <q-card-section>
                <div class="text-overline">Voting Deadline</div>
                <div class="text-h7 row">{{ VoteDeadline }}</div>
                <q-separator class="q-my-md" />
                <div class="text-overline">Nomination Deadline</div>
                <div class="text-h7 row">{{ NominationDeadline }}</div>
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
      expanded: false
    }
  },

  computed: {

  },

  props: {
    Title: {
      type: String,
      required: true
    },

    Description: {
      type: String,
      default: ''
    },

    VoteDeadline: {
      type: Object
    },

    NominationDeadline: {
      type: Object
    },

    Owner: {
      type: String,
      default: ''
    },

    Users: {
      type: Array,
      default: function () {
        return { Users: [] }
      }
    },

    Status: {
      type: Object,
      default: function () {
        return { Status: {} }
      }
    }
  }
}
</script>
