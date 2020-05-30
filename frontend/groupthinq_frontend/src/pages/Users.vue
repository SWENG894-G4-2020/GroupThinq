<template>
  <q-page class="flex flex-center" v-if='isLoaded'>
    <UserCard
      v-for="user in users"
      :key="user.userName"
      v-bind="user"
      class='q-pa-md q-ma-lg'
      />
  </q-page>
</template>

<script>
import UserCard from 'components/UserCard'
export default {
  name: 'PageUsers',

  components: {
    UserCard
  },

  methods: {
    loadUserData () {
      this.$axios.get('http://localhost:8080/users')
        .then(response => (this.users = response.data))
        .then(() => (this.isLoaded = true))
        .catch(error => (console.log(error)))
    }
  },

  mounted () {
    this.loadUserData()
  },

  data () {
    return {
      isLoaded: false,
      users: [
        {
          userName: 'jDoe',
          firstName: 'John',
          lastName: 'Doe',
          emailAddress: 'jDoe@foo.com'
        }
      ]
    }
  }
}
</script>
