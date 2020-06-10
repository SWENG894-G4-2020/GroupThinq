<template>
    <q-page padding class="column items-center justify-center">
      <q-btn color="green-8" label="New Decision" icon-right="add"  class="self-end q-mr-xl" @click="createDecision()"/>
      <DecisionCard
        v-for="decision in decisionList"
        :key="decision.UUID"
        v-bind="decision"
      />
      <q-dialog v-model="createDecisionDialog" persistent>
        <q-card>
          <q-card-section class='column items-center'>
            <div class="text-h5 q-pa-md"> Create A New Decision </div>
            <q-input filled class="q-mb-md" style="width: 100%" v-model="newDecision.name" label="Title" />
            <q-input filled class="q-my-md" style="width: 100%" v-model="newDecision.expiration" mask="date" :rules="['date']" label="Decision Expiration">
              <template v-slot:append>
                <q-icon name="event" class="cursor-pointer">
                  <q-popup-proxy ref="qDateProxy" transition-show="scale" transition-hide="scale">
                    <q-date v-model="newDecision.expiration" @input="() => $refs.qDateProxy.hide()" />
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
import DecisionCard from 'components/DecisionCard'

export default {
  name: 'PageDecisions',

  components: {
    DecisionCard
  },

  data () {
    return {
      createDecisionDialog: true,
      decisionList: [
        {
          UUID: '12341234-3245sdfg1345',
          Title: 'Main Decision Title',
          // eslint-disable-next-line no-multi-str
          Description: 'Decision description. Dolorem numquam nor nesciunt and adipisci, explicabo ratione. \
                      Architecto aut. Ab esse ad. Esse. Accusantium quo. Id rem dolorem nor \
                      sequi dolor yet adipisci. Quam do et anim dolorem. Dolore officia labore \
                      anim pariatur architecto, cillum. Aut anim so do. Vel. Si nostrud quia, \
                      for iure so lorem. Veritatis nostrum yet nisi yet ullamco.',
          VoteDeadline: new Date('2020-09-01T09:26:00-04:00'),
          NominationDeadline: 'Disabled for this decision.',
          Owner: 'MBoyer',
          Users: [
            'JDoe',
            'PMcCartney',
            'FMiller'
          ],
          Status: {
            vote: true,
            nominate: false,
            results: false
          },
          Recurring: false,
          Recurrance: 'None'
        }
      ],
      newDecision: {
        Name: '',
        expiration: ''
      }
    }
  },

  methods: {
    createDecision () {
      this.createDecisionDialog = true
    },

    onCancel () {
      this.createDecisionDialog = false
    },

    onCreate () {

    }
  }
}
</script>
