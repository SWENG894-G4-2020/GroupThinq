<template>
  <q-card style="">
    <q-card-section class="q-pa-md column items-center" v-if="winnerInfo">
      The winner is:
      {{winnerInfo.title}}
      {{winnerInfo.description}}
    </q-card-section>
    <q-card-section v-else>
      No results to show.
    </q-card-section>
    <q-card-section class="q-pa-md column items-center" >
      Results Graph Here
    </q-card-section>
    <q-card-actions align="right">
      <q-btn label="Close" @click="$emit('resultsClose')" />
    </q-card-actions>
  </q-card>
</template>

<script>
export default {
  name: 'ResultsTable',
  data () {
    return {
      columns: ''
    }
  },

  computed: {

  },

  props: {
    ballotOptions: {
      type: Array,
      required: true
    },
    results: {
      type: Array,
      required: true
    },
    resultTotals: {
      type: Object,
      required: true
    }
  }
}
</script>
