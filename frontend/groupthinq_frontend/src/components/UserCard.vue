<template>
  <q-card class="myCard" flat bordered>
      <q-card-section horizontal>
        <q-card-section class="q-pt-xs">
          <div class="text-overline">{{userName}}</div>
          <div class="text-h5 q-mt-sm q-mb-xs">{{firstName}} {{lastName}}</div>
        </q-card-section>
      </q-card-section>
      <q-separator />

      <q-card-section>
      <div class="text-caption text-grey">{{emailAddress}}</div>
      </q-card-section>
    </q-card>
</template>

<script>
export default {
  name: 'UserCard',
  props: {
    userName: {
      type: String,
      required: true
    },

    lastName: {
      type: String,
      default: ''
    },

    firstName: {
      type: String,
      default: ''
    },

    emailAddress: {
      type: String,
      default: ''
    }
  }
}
</script>
