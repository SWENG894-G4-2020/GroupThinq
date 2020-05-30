<template>
  <q-card elevated bordered class="q-pa-lg">
    <q-card-section>
      <div class="text-h4">Sign Up for GroupThinq!</div>
    </q-card-section>
    <q-card-section>
      <q-input filled class="q-my-md" v-model="FirstName" label="First Name" />
      <q-input filled class="q-my-md" v-model="LastName" label="Last Name" />
      <q-input filled class="q-my-md" v-model="EmailAddress" label="Email Address" />
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
          label="Sign Up"
          class="bg-primary text-grey-3 q-mx-sm"
          size="lg"
          @click="signUp()" />
      </div>
    </q-card-actions>

  </q-card>
</template>

<script>
export default {
  name: 'SignUpCard',

  data () {
    return {
      FirstName: '',
      LastName: '',
      EmailAddress: '',
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
    signUp () {
      this.$axios.post(`http://localhost:8080/users/${this.UserName}`,
        {
          userName: this.UserName,
          firstName: this.FirstName,
          lastName: this.LastName,
          emailAddress: this.EmailAddress
        })
        .then(this.$router.push('/main'))
        .catch(error => (console.log(error)))
    }
  }
}
</script>
