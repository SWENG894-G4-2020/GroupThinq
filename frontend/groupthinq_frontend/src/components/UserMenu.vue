<template>
  <q-btn color="primary" :label="userName" name="usermenu-btn"  icon-right="keyboard_arrow_down">
    <q-menu>
      <q-list style="min-width: 150px">
        <q-item clickable v-close-popup name="usermenu-setting" @click="goToSettings()">
          <q-item-section>Account Settings</q-item-section>
        </q-item>
        <q-separator />
        <q-item clickable v-close-popup name="usermenu-logout"  @click="logout()">
          <q-item-section class="text-negative">Logout</q-item-section>
        </q-item>
      </q-list>
    </q-menu>
  </q-btn>
</template>

<script>
import auth from '../store/auth'
export default {
  name: 'UserMenu',
  data () {
    return {
      userName: ''
    }
  },

  mounted () {
    this.userName = auth.getTokenData().sub
  },

  methods: {
    goToSettings () {
      this.$router.push('/main/account')
    },

    logout () {
      auth.removeTokens()
      this.$router.push('/')
    }
  }
}
</script>
