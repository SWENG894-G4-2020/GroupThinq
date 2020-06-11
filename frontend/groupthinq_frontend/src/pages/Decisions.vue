<template>
    <q-page padding class="column">
      <div v-if="isLoaded" class="column items-center">
        <q-btn color="green-8" label="New Decision" icon-right="add"  class="self-start" @click="createDecision()"/>
        <DecisionCard
          v-for="decision in decisionList.slice().sort((a,b) => a.expiration - b.expiration)"
          :key="decision.title"
          v-bind="decision"
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
            <q-input filled class="q-mb-md" style="width: 100%" v-model="newDecision.title" label="Title" />
            <q-input filled v-model="newDecision.expiration">
              <template v-slot:prepend>
                <q-icon name="event" class="cursor-pointer">
                  <q-popup-proxy transition-show="scale" transition-hide="scale">
                    <q-date v-model="newDecision.expiration" mask="YYYY-MM-DD HH:mm" />
                  </q-popup-proxy>
                </q-icon>
              </template>

              <template v-slot:append>
                <q-icon name="access_time" class="cursor-pointer">
                  <q-popup-proxy transition-show="scale" transition-hide="scale">
                    <q-time v-model="newDecision.expiration" mask="YYYY-MM-DD HH:mm" />
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
import DecisionCard from 'components/DecisionCard'

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
      decisionList: [
        {
          UUID: '12341234-3245sdfg1345',
          title: 'Main Decision Title',
          // eslint-disable-next-line no-multi-str
          description: 'Decision description. Dolorem numquam nor nesciunt and adipisci, explicabo ratione. \
                      Architecto aut. Ab esse ad. Esse. Accusantium quo. Id rem dolorem nor \
                      sequi dolor yet adipisci. Quam do et anim dolorem. Dolore officia labore \
                      anim pariatur architecto, cillum. Aut anim so do. Vel. Si nostrud quia, \
                      for iure so lorem. Veritatis nostrum yet nisi yet ullamco.',
          expiration: new Date('2020-09-01T09:26:00-04:00'),
          owner: 'MBoyer',
          users: [
            'JDoe',
            'PMcCartney',
            'FMiller'
          ]
        }
      ],
      newDecision: {}
    }
  },

  methods: {
    getData () {
      this.$axios.get(`${process.env.BACKEND_URL}/decisions`)
        .then(response => (this.decisionList = response.data))
        .then(() => (this.isLoaded = true))
        .catch(error => {
          console.log(error)
          this.isError = true
        })
    },

    createDecision () {
      this.resetNewDecision()
      this.createDecisionDialog = true
    },

    onCancel () {
      this.resetNewDecision()
      this.createDecisionDialog = false
    },

    onCreate () {
      this.decisionList.push(
        {
          title: this.newDecision.title,
          description: this.newDecision.description,
          expiration: new Date(this.newDecision.expiration),
          owner: auth.getTokenData().sub,
          users: []
        })
      this.createDecisionDialog = false
      /*
      this.$axios.post(`${process.env.BACKEND_URL}/decision/${this.UUID}`, this.newDecision)
        .then(() => {
          this.resetNewDecision()
          this.createDecisionDialog = false
        })
        .catch(error => {
          console.log(error)
          this.isError = true
        })
      */
    },

    resetNewDecision () {
      this.newDecision = {
        title: '',
        // eslint-disable-next-line no-multi-str
        description: 'Decision description. Dolorem numquam nor nesciunt and adipisci, explicabo ratione. \
                      Architecto aut. Ab esse ad. Esse. Accusantium quo. Id rem dolorem nor \
                      sequi dolor yet adipisci. Quam do et anim dolorem. Dolore officia labore \
                      anim pariatur architecto, cillum. Aut anim so do. Vel. Si nostrud quia, \
                      for iure so lorem. Veritatis nostrum yet nisi yet ullamco.',
        expiration: '',
        users: []
      }
    }
  },

  mounted () {
    this.isLoaded = true
    // this.getData()
  }
}
</script>
