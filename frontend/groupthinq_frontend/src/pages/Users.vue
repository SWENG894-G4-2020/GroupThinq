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
import UserCard from 'src/components/UserCard'
export default {
  name: 'PageUsers',

  components: {
    UserCard
  },

  methods: {
    async loadUserData () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/users`)
        this.users = response.data
        this.isLoaded = true
      } catch (error) {
        console.log(error)
      }
    }
  },

  mounted () {
    this.loadUserData()
  },

  data () {
    return {
      isLoaded: false,
      users: []
    }
  }
}
</script>
