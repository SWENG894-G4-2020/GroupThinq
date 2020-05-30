<template>
  <q-card elevated bordered class="q-pa-lg">
    <q-card-section>
      <div class="text-h4">Login</div>
    </q-card-section>
    <q-card-section>
      <q-input filled class="q-my-md" v-model="UserName" label="Username" />
      <q-input filled class="q-my-md" v-model="Password" type="password" label="Password" />
    </q-card-section>
    <q-card-actions align="right">
      <div class='row items-end'>
        <q-btn
          class="q-mx-sm"
          label="Cancel"
          @click="cancel()" />
        <q-btn
          label="Login"
          class="bg-primary text-grey-3 q-mx-sm"
          size="lg"
          @click="login()" />
      </div>
    </q-card-actions>

  </q-card>
</template>

<script>
export default {
  name: 'LoginCard',

  data () {
    return {
      UserName: '',
      Password: ''
    }
  },

  props: {
  },

  methods: {
    cancel () {
      this.$router.push('/')
    },
    login () {
      this.$axios.post('http://localhost:8080/login',
        {
          userName: this.UserName,
          password: this.Password
        })
        .then(this.$router.push('/main'))
        .catch(error => (console.log(error)))
    }
  }
}
</script>
