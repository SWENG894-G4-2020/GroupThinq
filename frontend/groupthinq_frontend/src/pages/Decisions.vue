<template>
    <q-page padding class="column">
      <div v-if="isLoaded" class="column items-center">
        <q-btn color="green-8" label="New Decision" icon-right="add"  class="self-start" @click="createDecision()"/>
        <div class="text-h5 text-primary" v-if="decisionList.length == 0">No decisions? Make a new one!</div>
        <DecisionCard
          v-for="decision in decisionList.slice().sort((a,b) => new Date(a.expirationDate) - new Date(b.expirationDate))"
          :key="decision.name"
          v-bind="decision"
          @needReload="getData()"
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
      <q-dialog v-model="createDecisionDialog" persistent>
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
      </q-dialog>
    </q-page>
</template>

<script>
import auth from '../store/auth'
import DecisionCard from 'src/components/DecisionCard'

export default {
  name: 'PageDecisions',

  components: {
    DecisionCard
  },

  data () {
    return {
      isLoaded: false,
      isError: false,
      createDecisionDialog: false,
      decisionList: [],
      newDecision: {}
    }
  },

  methods: {
    async getData () {
      try {
        const userName = auth.getTokenData().sub
        const response = await this.$axios.get(`${process.env.BACKEND_URL}/users/${userName}/decision`)
        this.decisionList = response.data
        this.isLoaded = true
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    },

    createDecision () {
      this.resetNewDecision()
      this.createDecisionDialog = true
    },

    onCancel () {
      this.resetNewDecision()
      this.createDecisionDialog = false
    },

    async onCreate () {
      this.newDecision.expirationDate = new Date(this.newDecision.expirationDate).toISOString()
      try {
        await this.$axios.post(`${process.env.BACKEND_URL}/decision/`, this.newDecision)
        this.resetNewDecision()
        this.createDecisionDialog = false
        this.getData()
      } catch (error) {
        console.log(error)
        this.isError = true
      }
    },

    resetNewDecision () {
      this.newDecision = {
        name: '',
        description: '',
        expirationDate: '',
        ownerUsername: auth.getTokenData().sub
      }
    }
  },

  mounted () {
    // this.isLoaded = true
    this.getData()
  }
}
</script>
