<template>
  <q-page class="q-pa-md">
    <q-input
      v-model="search"
      debounce="0"
      filled
      placeholder="Search"
      class="full-width q-pa-sm"
      >
      <template v-slot:append>
        <q-icon name="search" />
      </template>
    </q-input>
    <div v-if='isLoaded' class="row">
      <UserCard
      class="col-xs-12 col-sm-6 col-md-4 col-lg-3"
      v-for="user in filteredUsers"
      :key="user.userName"
      v-bind="user"
      />
    </div>
    <div v-else-if="!isError">
      <div class="text-h5 text-primary">Loading...
        <q-spinner-hourglass color="primary" size="2em"/>
      </div>
    </div>
    <div v-else>
      <div class="text-h5 text-negative self-center">
        Something went wrong.
        <q-icon name="warning" />
      </div>
      <div class="text-h7 text-negative self-center">Please refresh the page.
      </div>
    </div>
  </q-page>
</template>

<script>
import UserCard from 'src/components/UserCard'
export default {
  name: 'PageUsers',

  components: {
    UserCard
  },

  data () {
    return {
      isLoaded: false,
      isError: false,
      users: [],
      search: ''
    }
  },

  mounted () {
    this.loadUserData()
  },

  computed: {
    filteredUsers: function () {
      return this.users.filter(user =>
        user.userName.toLowerCase().includes(this.search.toLowerCase()) ||
        user.firstName.toLowerCase().includes(this.search.toLowerCase()) ||
        user.lastName.toLowerCase().includes(this.search.toLowerCase())
      )
    }
  },

  methods: {
    async loadUserData () {
      try {
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/users`)
        this.users = response.data.data
        this.isLoaded = true
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    }
  }
}
</script>
