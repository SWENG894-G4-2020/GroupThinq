<template>
  <q-page class="flex flex-center">
    <div class='col'>
    <DecisionCard
      v-for="decision in decisionList"
      :key="decision.UUID"
      v-bind="decision"
    />
    </div>
  </q-page>
</template>

<script>
import DecisionCard from 'components/DecisionCard'

export default {
  name: 'PageIndex',

  components: {
    DecisionCard
  },

  data () {
    return {
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
      ]
    }
  }
}
</script>
