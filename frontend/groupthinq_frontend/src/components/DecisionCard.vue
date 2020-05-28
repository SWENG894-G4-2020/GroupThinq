<template>
  <q-card elevated bordered class="q-ma-xl">
    <q-card-section>
      <div class="text-h5 q-mt-sm q-mb-xs">{{Title}}</div>
      <div
        class="text-caption text-grey"
      >{{Description}}</div>
    </q-card-section>
    <q-card-actions>
      <q-btn
        color="grey"
        round
        flat
        dense
        :icon="expanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
        @click="expanded = !expanded"
      />
    </q-card-actions>

    <q-slide-transition>
      <div v-show="expanded">
        <q-separator />
        <q-card-section class="text-subitle2">{{ Owner }}</q-card-section>
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

  props: {
    Title: {
      type: String,
      required: true
    },

    Description: {
      type: String,
      default: ''
    },

    Owner: {
      type: String,
      default: ''
    },

    Users: {
      type: String,
      default: ''
    }
  }
}
</script>
