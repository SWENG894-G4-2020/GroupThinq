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
      </transition-group>
      <q-btn round icon="edit">
        <q-menu>
          <q-list>
            <q-item clickable v-close-popup @click="editEnabled=!editEnabled">
              <q-item-section>Edit</q-item-section>
            </q-item>
            <q-separator />
            <q-item clickable v-close-popup @click="deleteConfirm = true">
              <q-item-section class="text-negative">DELETE</q-item-section>
            </q-item>
          </q-list>
        </q-menu>
      </q-btn>
    </q-card-actions>
    <q-card-section>
      <q-input filled dense class="q-mb-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="userInfo.firstName" label="First Name" />
      <q-input filled dense class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="userInfo.lastName" label="Last Name" />
      <q-input filled dense class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="userInfo.emailAddress" label="Email Address" />
      <q-input filled dense class="q-my-md" :readonly="!editEnabled" :bg-color="fieldColor" v-model="userInfo.birthDate" label="Birthdate" />
      <q-input filled dense class="q-my-md" readonly v-model="userInfo.userName" label="Username" />
      <q-input filled dense class="q-mt-md" readonly v-model="userInfo.password" type="password" label="Password" />
      <q-input filled dense class="q-mt-md" readonly v-model="userInfo.createdDate" label="Created On" />
      <q-input filled dense class="q-mt-md" readonly v-model="userInfo.updatedDate" label="Last Updated" />
      <q-input filled dense class="q-mt-md" readonly v-model="userInfo.lastLoggedIn" label="Last Logged In" />
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
import auth from '../store/auth'
export default {
  name: 'AccountInfoCard',

  data () {
    return {
      deleteConfirm: false,
      editEnabled: false,
      storedUserName: '',

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
    }
  },

  mounted () {
    this.storedUserName = auth.getTokenData().sub
    console.log(this.storedUserName)
    this.getData()
  },

  methods: {
    onCancel () {
      this.deleteConfirm = false
      this.editEnabled = false
      this.getData()
    },
    onConfirm () {
      this.updatedDate = Date.now()
      this.$axios.put(`http://localhost:8080/users/${this.storedUserName}`,
        this.userInfo)
        .then(() => { this.$router.push('/main/account') })
        .catch(error => (console.log(error)))

      this.editEnabled = false
    },
    onDelete () {
      this.$axios.delete(`http://localhost:8080/users/${this.storedUserName}`)
        .then(() => { this.$router.push('/') })
        .catch(error => (console.log(error)))

      this.$router.push('/')
    },
    getData () {
      this.$axios.get(`http://localhost:8080/users/${this.storedUserName}`)
        .then((response) => {
          console.log(response.data)
          this.userInfo = response.data
        })
        .catch(error => (console.log(error)))
    }
  }
}
</script>
