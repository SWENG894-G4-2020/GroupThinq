<template>
<div class="q-pa-sm">
  <q-card bordered>
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
  <q-card-section v-if="currentUserRole === 'Admin'">
      <q-btn icon="edit" label="Admin Edit" name="admin-edit" :to="'/account/' + userName"/>
  </q-card-section>
  </q-card>
</div>
</template>

<script>
import auth from 'src/store/auth'

export default {
  name: 'UserCard',

  data () {
    return {
      currentUserRole: 'User'
    }
  },

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
  },

  mounted () {
    this.currentUserRole = auth.getTokenData().role
  }
}
</script>
