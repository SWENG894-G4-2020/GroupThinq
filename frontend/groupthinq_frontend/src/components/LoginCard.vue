<template>
  <q-card elevated bordered class="q-pa-lg">
    <q-card-section>
      <div class="text-h4">Login</div>
    </q-card-section>
    <q-card-section>
      <q-input filled class="q-my-md" v-model="userName" label="Username" />
      <q-input filled class="q-my-md" v-model="password" type="password" label="Password" />
    </q-card-section>
    <q-card-actions align="right">
      <div class="row items-end">
        <q-btn class="q-mx-sm" label="Cancel" @click="cancel()" />
        <q-btn label="Login" class="bg-primary text-grey-3 q-mx-sm" size="lg" @click="login()" />
      </div>
    </q-card-actions>
  </q-card>
</template>

<script>
import auth from 'src/store/auth'
export default {
  name: 'LoginCard',

  data () {
    return {
      userName: '',
      password: ''
    }
  },

  props: {},

  methods: {
    cancel () {
      this.$router.push('/')
    },
    login () {
      this.$axios
        .post(`${process.env.BACKEND_URL}/login`, {
          userName: this.userName,
          password: this.password
        })
        .then((response) => {
          auth.storeToken(response.headers.authorization)
        })
        .then(() => {
          this.$router.push('/main')
            .catch(error => { console.log(`Vue router error: ${error}`) })
        })
        .catch(error => {
          console.log(error)
          this.$router.push('/login')
        })
    }
  }
}
</script>
