<template>
  <q-card elevated bordered class="q-pa-lg">
    <q-card-section>
      <div class="text-h4">Sign Up for GroupThinq!</div>
    </q-card-section>
    <q-card-section>
      <q-input filled class="q-my-md" v-model="newUser.firstName" label="First Name" />
      <q-input filled class="q-my-md" v-model="newUser.lastName" label="Last Name" />
      <q-input filled class="q-my-md" v-model="newUser.emailAddress" label="Email Address" />
      <q-input filled class="q-my-md" v-model="newUser.birthDate" mask="date" :rules="['date']" label="Birth Date">
        <template v-slot:append>
          <q-icon name="event" class="cursor-pointer">
            <q-popup-proxy ref="qDateProxy" transition-show="scale" transition-hide="scale">
              <q-date v-model="newUser.birthDate" @input="() => $refs.qDateProxy.hide()" />
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>
      <q-input filled class="q-my-md" v-model="newUser.userName" label="Username" />
      <q-input filled class="q-my-md" v-model="newUser.password" type="password" label="Password" />
    </q-card-section>
    <q-card-actions align="right">
      <div class='row items-end'>
        <q-btn
          class="q-mx-sm"
          label="Cancel"
          @click="cancel()" />
        <q-btn
          label="Sign Up"
          class="bg-primary text-grey-3 q-mx-sm"
          size="lg"
          @click="signUp()" />
      </div>
    </q-card-actions>

  </q-card>
</template>

<script>
import auth from 'src/store/auth'
export default {
  name: 'SignUpCard',

  data () {
    return {
      newUser: {
        firstName: '',
        lastName: '',
        emailAddress: '',
        birthDate: '',
        userName: '',
        password: ''
      }
    }
  },

  props: {
  },

  methods: {
    cancel () {
      this.$router.push('/')
    },
    async signUp () {
      const isoDate = new Date(this.newUser.birthDate).toISOString()
      try {
        await this.$axios.post(`${process.env.BACKEND_URL}/users/${this.newUser.userName}`,
          {
            firstName: this.newUser.firstName,
            lastName: this.newUser.lastName,
            emailAddress: this.newUser.emailAddress,
            birthDate: isoDate,
            userName: this.newUser.userName,
            password: this.newUser.password
          })
        const loginResponse = await this.$axios.post(`${process.env.BACKEND_URL}/login`,
          {
            userName: this.newUser.userName,
            password: this.newUser.password
          })
        auth.storeToken(loginResponse.headers.authorization)
        this.$router.push('/main')
      } catch (error) {
        console.log(error)
        this.$router.push('/login')
      }
    }
  }
}
</script>
