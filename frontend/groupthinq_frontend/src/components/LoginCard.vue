<template>
  <q-card elevated bordered class="q-pa-lg" style="width:50%">
    <q-card-section>
      <div class="text-h4">Login</div>
    </q-card-section>
    <q-card-section>
      <q-input filled class="q-my-md" v-model="userName" label="Username" />
      <q-input filled class="q-mt-md" v-model="password" type="password" label="Password" />
    </q-card-section>
    <q-card-section v-if="isError">
      <div id="LoginError" class="text-body text-negative text-center"> An invalid login has been provided. Please try again. </div>
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
      password: '',
      isError: false
    }
  },

  props: {},

  mounted () {
    // this is neccesary in case a browser has stale JWTs from a previous session
    auth.removeTokens()
  },

  methods: {
    cancel () {
      this.$router.push('/')
    },
    login () {
      this.isError = false
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
          this.isError = true
          console.log(error)
        })
    }
  }
}
</script>
