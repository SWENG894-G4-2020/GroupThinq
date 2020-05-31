<template>
  <div>
  <q-card bordered class="q-px-sm">
    <q-card-actions align="right">
      <transition-group
        appear
        enter-active-class="animated fadeIn"
        leave-active-class="animated fadeOut"
        class="row items-center"
      >
        <div key="editText" v-if="editEnabled" class="text-grey-5">Editing... </div>
        <q-btn key="deleteButton" v-if="editEnabled" flat round color="red" @click="deleteConfirm = true" icon="delete_forever" />
      </transition-group>
      <q-btn flat round icon="edit" :disable="editEnabled" @click="editEnabled=!editEnabled" />
    </q-card-actions>
    <q-card-section>
      <q-input filled class="q-mb-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="FirstName" label="First Name" />
      <q-input filled class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="LastName" label="Last Name" />
      <q-input filled class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="EmailAddress" label="Email Address" />
      <q-input filled class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="UserName" label="Username" />
      <q-input filled class="q-mt-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="Password" type="password" label="Password" />
    </q-card-section>
    <transition appear enter-active-class="animated fadeIn" leave-active-class="animated fadeOut">
      <q-card-actions key="buttons" v-if="editEnabled" align="right">
        <q-btn label="cancel" @click="onCancel()" />
        <q-btn color="primary" @click="onConfirm()" label="confirm edit" />
      </q-card-actions>
    </transition>
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
export default {
  name: 'AccountInfoCard',

  methods: {
    onCancel () {
      this.deleteConfirm = false
      this.editEnabled = false
      this.getData()
    },
    onConfirm () {
      // this.$axios.put(`http://localhost:8080/users/${this.UserName}`,
      //   {
      //     userName: this.UserName,
      //     firstName: this.FirstName,
      //     lastName: this.LastName,
      //     emailAddress: this.EmailAddress,
      //     password: this.Password
      //   })
      //   .then(this.$router.push('/main'))
      //   .catch(error => (console.log(error)))

      this.editEnabled = false
    },
    onDelete () {
      // this.$axios.delete('http://localhost:8080/users/$(this.UserName)')
      //   .then(() => (this.$router.push('/')))
      //   .catch(error => (console.log(error)))

      this.$router.push('/')
    },
    getData () {
      // this.$axios.get('http://localhost:8080/users')
      //   .then(response => (this.users = response.data))
      //   .then(() => (this.isLoaded = true))
      //   .catch(error => (console.log(error)))
      //
      // this is just a placeholder for the UI
      this.FirstName = 'First'
      this.LastName = 'Last'
      this.EmailAddress = 'FirstLast@foo.com'
      this.UserName = 'flTest'
      this.Password = 'test'
    }
  },

  computed: {
    fieldColor: function () {
      return this.editEnabled ? 'blue-1' : ''
    }
  },

  data () {
    return {
      deleteConfirm: false,
      editEnabled: false,
      FirstName: 'First',
      LastName: 'Last',
      EmailAddress: 'FirstLast@foo.com',
      UserName: 'flTest',
      Password: 'test'
    }
  }
}
</script>
