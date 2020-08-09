<template>
  <div>
    <q-card bordered class="q-px-sm">
      <q-card-section>
        <div class="text-h5 q-py-md"><q-icon name="account_box" color="grey-7"/> My Account</div>
      </q-card-section>
      <q-card-section>
        <q-input dense class="q-mb-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="userInfo.firstName" :rules="[val => !!val || '*Required']" label="First Name" />
        <q-input dense class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="userInfo.lastName" :rules="[val => !!val || '*Required']" label="Last Name" />
        <q-input dense class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="userInfo.emailAddress" :rules="[val => !!val || '*Required']" label="Email Address" />
        <q-input dense class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="userInfo.birthDate" label="Birthdate" />
        <q-input dense class="q-my-md" readonly v-model="userInfo.userName" label="Username" />
        <q-input dense class="q-mt-md" readonly v-model="userInfo.password" type="password" label="Password" />
        <q-input dense class="q-mt-md" readonly v-model="userInfo.createdDate" label="Created On" />
        <q-input dense class="q-mt-md" readonly v-model="userInfo.updatedDate" label="Last Updated" />
        <q-input dense class="q-mt-md" readonly v-model="userInfo.lastLoggedIn" label="Last Logged In" />
      </q-card-section>
      <q-card-section>
        <div class="row reverse q-gutter-sm">
          <q-btn v-if="(currentUserName === userInfo.userName || currentUserRole === 'Admin') && editEnabled" class="col-xs-12 col-sm-auto" size="lg" icon="check" color="positive" label="Confirm" name="account-confirm" @click="onConfirm()"/>
          <q-btn v-if="(currentUserName === userInfo.userName || currentUserRole === 'Admin') && !editEnabled" class="col-xs-12 col-sm-auto" size="lg" icon="edit" label="Edit" name="account-edit" @click="editEnabled=true"/>
          <q-btn v-if="currentUserName === userInfo.userName || currentUserRole === 'Admin'" class="col-xs-12 col-sm-auto" icon="delete" size="lg" color="negative" label="Delete" @click="deleteConfirm = true" name="account-delete"/>
          <q-btn v-if="(currentUserName === userInfo.userName || currentUserRole === 'Admin') && editEnabled" class="col-xs-12 col-sm-auto" icon="close" size="lg" label="Cancel" name="account-edit-cancel" @click="onCancel()"/>
        </div>
      </q-card-section>
    </q-card>
    <q-dialog v-model="deleteConfirm" persistent>
      <q-card>
        <q-card-section class='column items-center'>
          <div class="text-grey-8"> Are you sure you want to delete your account?</div>
          <div class="text-red-6"> This action cannot be undone!</div>
        </q-card-section>
        <q-card-actions align="right">
          <q-btn label="cancel" @click="onCancel()" />
          <q-btn color="red" @click="onDelete()" label="Confirm Deletion" />
        </q-card-actions>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import auth from 'src/store/auth'
export default {
  name: 'AccountInfoCard',

  data () {
    return {
      deleteConfirm: false,
      editEnabled: false,
      currentUserName: '',
      currentUserRole: 'User',

      userInfo: {
        birthDate: '',
        createdDate: '',
        emailAddress: '',
        firstName: '',
        lastLoggedIn: '',
        lastName: '',
        password: '',
        updatedDate: '',
        userName: ''
      }
    }
  },

  computed: {
    fieldColor: function () {
      return this.editEnabled ? 'blue-1' : ''
    },
    validInputs: function () {
      if (this.userInfo.firstName.length < 1) { return false }
      if (this.userInfo.lastName.length < 1) { return false }
      if (this.userInfo.emailAddress.length < 1) { return false }
      return true
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.currentUserRole = auth.getTokenData().role
    this.getData()
  },

  methods: {
    onCancel () {
      this.deleteConfirm = false
      this.editEnabled = false
      this.getData()
    },

    async onConfirm () {
      if (!this.validInputs) { return }
      this.updatedDate = Date.now()
      await this.$axios.put(`${process.env.BACKEND_URL}/user/${this.userInfo.userName}`,
        this.userInfo)
        .then(this.getData())
        .catch(error => (console.log(error)))
      this.editEnabled = false
      await this.getData()
    },

    onDelete () {
      this.$axios.delete(`${process.env.BACKEND_URL}/user/${this.userInfo.userName}`)
        .then(() => { this.$router.push('/') })
        .catch(error => (console.log(error)))
    },

    getData () {
      let name = this.currentUserName
      if (this.currentUserRole === 'Admin' && this.$route.params.user) {
        name = this.$route.params.user
      }

      this.$axios.get(`${process.env.BACKEND_URL}/user/${name}`)
        .then((response) => {
          this.userInfo = response.data.data[0]
        })
        .catch(error => (console.log(error)))
    }
  }
}
</script>

<style>
.q-field--readonly .q-field__control:before {
    border-bottom-style: none !important;
}
</style>
