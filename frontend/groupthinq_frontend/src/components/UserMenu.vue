<template>
  <q-btn dense class="q-mx-sm" label="logout" @click="logout()" />
</template>

<script>
import auth from '../store/auth'
export default {
  name: 'UserMenu',
  data () {
    return {}
  },
  methods: {
    goToSettings () {

    },

    logout () {
      auth.removeTokens()
      this.$router.push('/')
    }
  }
}
</script>
