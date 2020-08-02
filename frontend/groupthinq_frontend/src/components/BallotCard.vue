<template>
  <q-card bordered style="height: 100%">
    <q-card-section class="q-pb-sm">
      <div class="text-h5"><q-icon name="ballot" /> Ballot</div>
      <ExpirationField v-bind:mode="mode" ref="datetime" @expired="setExpired"/>
      <div class="q-mt-sm">
        <div class="text-grey-8" style="font-size: 16px"> Voting Method</div>
        <q-btn-toggle
          v-if="mode === 'create'"
          spread
          v-model="ballotTypeId"
          toggle-color="primary"
          :options="ballotTypeOptions"
        />
        <div v-else class="q-py-sm">{{ ballotTypeOptions.find(bt => bt.value === ballotTypeId ).label }}</div>
        <div class="q-py-sm text-grey-7" style="min-height: 62px">{{ ballotTypeOptions.find(bt => bt.value === ballotTypeId ).description }}</div>
      </div>
    </q-card-section>
    <q-card-section class="q-pt-none">
      <div class="text-grey-8" style="font-size: 16px"> Ballot choices</div>
      <q-input class="q-mb-md" v-model="newOption.title" label="Add Choice" >
        <template v-slot:append>
          <q-btn dense color="positive" icon="add" @click="addDecisionOption()" />
        </template>
      </q-input>
      <div class="column">
        <div v-for="(option,idx) in sortedOptions" :key="idx" class="row items-center q-mb-sm" style="width: 100%">
          <q-checkbox v-if="ballotTypeId === 1 && mode !== 'create'" v-model="option.rank" />
          <q-avatar v-else-if="mode !== 'create'" size="md">{{ option.rank ? option.rank : idx + 1 }}</q-avatar>
          <span class="text-body1 col-grow">{{option.title}}</span>
          <q-btn v-if="ballotTypeId === 2 && mode !== 'create'" flat color="positive" icon="expand_less" @click="removeDecisionOption(option)" />
          <q-btn v-if="ballotTypeId === 2 && mode !== 'create'" flat color="negative" icon="expand_more" @click="removeDecisionOption(option)" />
          <q-btn v-if="mode === 'create'" flat color="negative" icon="close" @click="removeDecisionOption(option)" />
        </div>
      </div>
      <div class="row reverse q-gutter-sm">
        <q-btn v-if="!expired && mode === 'view'" icon="check" color="primary" label="Vote" />
      </div>
    </q-card-section>
  </q-card>
</template>

<script>
import auth from 'src/store/auth'
import ExpirationField from 'src/components/ExpirationField'

export default {
  name: 'BallotCard',

  components: {
    ExpirationField
  },

  data () {
    return {
      currentUserName: '',
      ballotTypeId: 1,
      ballotTypeOptions: [
        {
          label: 'First Past the Post',
          value: 1,
          description: 'The choice with the most votes win. Voters pick one choice.'
        },
        {
          label: 'Ranked Pair',
          value: 2,
          description: 'Selects a single winner using votes that express preferences. Voters rank the choices.'
        }
      ],
      newOption: { title: '', userName: this.currentUserName },
      options: [],
      expired: false
    }
  },

  props: {
    decision: {
      type: Object,
      required: false
    },

    mode: {
      type: String,
      required: true
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
    this.newOption.userName = this.currentUserName
  },

  computed: {
    sortedOptions: function () {
      return this.options
    }
  },

  methods: {
    addDecisionOption () {
      if (this.newOption.title !== '') {
        this.options.push({ title: this.newOption.title, userName: this.currentUserName })
        this.newOption = { title: '', userName: this.currentUserName }
      }
    },

    removeDecisionOption (option) {
      const pos = this.options.indexOf(option)
      this.options.splice(pos, 1)
    },

    getExpiration () {
      return this.$refs.datetime.datetime
    },

    setExpired () {
      this.expired = true
    },

    isValid () {
      if (!this.$refs.datetime.isValid()) { return false }
      if (!this.ballotTypeId || this.ballotTypeId === '' || this.ballotTypeId < 1 || this.ballotTypeId > 2) { return false }
      if (this.options < 1) { return false }
      return true
    }
  }
}
</script>

<style scoped>
.q-field--readonly .q-field__control:before {
    border-bottom-style: none !important;
}
</style>
