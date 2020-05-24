<template>
  <q-page class="flex flex-center">
    <UserCard
      v-for="user in users"
      :key="user.userName"
      v-bind="user"
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
      console.log('In the loadUserData method.')
      this.$axios.get('http://backend:8080/users/')
        .then(response => (this.users = response.data))
        .catch(console.log('Something went wrong :('))
    }
  },

  mounted () {
    this.loadUserData()
  },

  data () {
    return {
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
