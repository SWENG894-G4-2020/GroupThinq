<template>
  <q-card>
    <q-card-section class='column items-center'>
      <div class="text-h5 q-pa-md"> Create A New Decision </div>
      <q-input filled class="q-mb-md" style="width: 100%" v-model="newDecision.name" label="Title" />
      <q-input filled type="textarea" class="q-mb-md" style="width: 100%" v-model="newDecision.description" label="Description" />
      <q-input filled v-model="newDecision.expirationDate">
        <template v-slot:prepend>
          <q-icon name="event" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-date v-model="newDecision.expirationDate" mask="YYYY-MM-DD HH:mm" />
            </q-popup-proxy>
          </q-icon>
        </template>

        <template v-slot:append>
          <q-icon name="access_time" class="cursor-pointer">
            <q-popup-proxy transition-show="scale" transition-hide="scale">
              <q-time v-model="newDecision.expirationDate" mask="YYYY-MM-DD HH:mm" />
            </q-popup-proxy>
          </q-icon>
        </template>
      </q-input>
    </q-card-section>
    <q-card-actions align="right">
      <q-btn label="cancel" @click="onCancel()" />
      <q-btn color="green-8" @click="onCreate()" label="Create Decision" />
    </q-card-actions>
  </q-card>
</template>

<script>
import auth from 'src/store/auth'
export default {
  name: 'CreateDecisionModal',
  data () {
    return {
      newDecision: {}
    }
  },

  mounted () {
    this.resetNewDecision()
  },

  methods: {
    onCancel () {
      this.$emit('createClose')
    },

    async onCreate () {
      this.newDecision.expirationDate = new Date(this.newDecision.expirationDate).toISOString()
      this.newDecision.includedUsers.push({ userName: 'foofoo' })
      try {
        await this.$axios.post(`${process.env.BACKEND_URL}/decision/`, this.newDecision)
        this.$emit('createClose')
      } catch (error) {
        console.log(error)
        this.$emit('error')
      }
    },

    resetNewDecision () {
      const currentUserName = auth.getTokenData().sub
      this.newDecision = {
        name: '',
        description: '',
        expirationDate: '',
        ownerUsername: currentUserName,
        includedUsers: [
          { userName: currentUserName }
        ]
      }
    }
  }
}
</script>
