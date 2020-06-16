<template>
<div>
  <q-card bordered class="q-mx-xl q-my-md">
    <q-card-section>
      <div class="text-h5 q-mb-xs row items-center">
        <div class="col">{{title}}</div>
        <q-btn round icon="edit" class="col-auto">
          <q-menu>
            <q-list>
              <q-item clickable v-close-popup @click="onEdit()">
                <q-item-section>Edit</q-item-section>
              </q-item>
              <q-separator />
              <q-item clickable v-close-popup @click="deleteDecisionDialog = true">
                <q-item-section class="text-negative">DELETE</q-item-section>
              </q-item>
            </q-list>
          </q-menu>
        </q-btn>
      </div>
      <div class="text-caption text-grey">{{description}}</div>
      <div class="text-negative" v-if="!expired">Remaining: {{daysRemaining}}d {{hoursRemaining}}h {{minutesRemaining}}m {{secondsRemaining}}s</div>
      <div class="text-negative" v-else>Voting has closed.</div>
    </q-card-section>
    <q-card-actions class="row justify-between">
      <div class="col">
        <q-btn
            color="grey"
            round
            flat
            dense
            :icon="expanded ? 'keyboard_arrow_up' : 'keyboard_arrow_down'"
            @click="expanded = !expanded"
        />
        DETAILS
      </div>
      <div class="justify-end">
          <q-btn label="View Results"
            class="q-mx-xs"
            :outline="!status.results"
            :disable="!status.results"
            color="primary" />
          <!--
          <q-btn label="Nominate"
            class="q-mx-xs"
            :outline="!status.nominate"
            :disable="!status.nominate"
            color="primary" />
            -->
          <q-btn label="Vote"
            class="q-mx-xs"
            :outline="!status.vote"
            :disable="!status.vote"
            color="primary" />
        </div>
    </q-card-actions>

    <q-slide-transition>
      <div v-show="expanded">
        <q-separator />
        <q-card-section horizontal>
            <q-card-section class="col-4 q-mx-md">
                <div class="text-overline">Owner</div>
                <div class="text-h7">{{ owner }}</div>
            <q-separator class="q-my-md" />
                <div class="text-overline">Members</div>
                <div class="text-h7 row"> not implemented. </div>
                <!-- <div class="text-h7 row" v-for="user in users" :key="user">{{ user }}</div> -->
            </q-card-section>
            <q-card-section>
                <div class="text-overline">Voting Deadline</div>
                <div class="text-h7 row">{{ expiration }}</div>
                <q-separator class="q-my-md" />
                <div class="text-overline">Nomination Deadline</div>
                <div class="text-h7 row"> not implemented. </div>
            </q-card-section>
        </q-card-section>
      </div>
    </q-slide-transition>
  </q-card>
  <q-dialog v-model="editDecisionDialog" persistent style="width: 500px">
    <q-card style="min-width: 400px">
      <q-card-section class='column items-center'>
        <div class="text-h5 q-pa-md"> Editing decision... </div>
        <q-input filled class="q-mb-md" style="width: 100%" v-model="editDetails.title" label="Title" />
        <q-input filled type="textarea" class="q-mb-md" style="width: 100%" v-model="editDetails.description" label="Description" />
        <q-input filled v-model="editDetails.expiration" style= "width: 100%" label="Expiration">
          <template v-slot:prepend>
            <q-icon name="event" class="cursor-pointer">
              <q-popup-proxy transition-show="scale" transition-hide="scale">
                <q-date v-model="editDetails.expiration" mask="YYYY-MM-DD HH:mm" />
              </q-popup-proxy>
            </q-icon>
          </template>

          <template v-slot:append>
            <q-icon name="access_time" class="cursor-pointer">
              <q-popup-proxy transition-show="scale" transition-hide="scale">
                <q-time v-model="editDetails.expiration" mask="YYYY-MM-DD HH:mm" />
              </q-popup-proxy>
            </q-icon>
          </template>
        </q-input>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn label="cancel" @click="onEditCancel()" />
        <q-btn color="green-8" @click="onConfirmEdit()" label="Confirm" />
      </q-card-actions>
    </q-card>
  </q-dialog>
  <q-dialog v-model="deleteDecisionDialog" persistent>
    <q-card>
      <q-card-section class='column items-center'>
        <div class="text-grey-8"> Are you sure you want to delete this decision?</div>
        <div class="text-red-6"> This action cannot be undone!</div>
      </q-card-section>
      <q-card-actions align="right">
        <q-btn label="cancel" @click="deleteDecisionDialog = false" />
        <q-btn color="red" @click="onConfirmDelete()" label="Confirm Deletion" />
      </q-card-actions>
    </q-card>
  </q-dialog>
</div>
</template>

<script>
export default {
  name: 'DecisionCard',

  data () {
    return {
      editDecisionDialog: false,
      deleteDecisionDialog: false,
      expanded: false,
      expired: false,
      daysRemaining: '',
      hoursRemaining: '',
      minutesRemaining: '',
      secondsRemaining: '',
      editDetails: {
        title: '',
        description: '',
        expiration: ''
      }
    }
  },

  computed: {
    status: function () {
      if (!this.expired) {
        return { vote: true, nominate: false, results: false }
      } else {
        return { vote: false, nominate: false, results: true }
      }
    }
  },

  props: {
    id: {
      type: Number,
      required: true
    },

    title: {
      type: String,
      required: true
    },

    description: {
      type: String,
      default: ''
    },

    expiration: {
      type: Date,
      required: true
    },

    owner: {
      type: String,
      default: ''
    },

    users: {
      type: Array,
      default: function () {
        return []
      }
    }
  },

  mounted () {
    this.calculateRemainingTime()
  },

  methods: {
    calculateRemainingTime () {
      const secondsTiemr = setInterval(() => {
        const diff = (this.expiration - Date.now()) / 1000

        if (diff < 0) {
          clearInterval(secondsTiemr)
          this.expired = true
          return
        }

        const days = Math.floor(diff / (3600 * 24))
        const hours = Math.floor((diff % (3600 * 24)) / 3600)
        const minutes = Math.floor((diff % 3600) / 60)
        const seconds = Math.floor((diff % 60))
        this.daysRemaining = days
        this.hoursRemaining = hours
        this.minutesRemaining = minutes
        this.secondsRemaining = seconds
      }, 1000)
    },

    onEdit () {
      this.editDecisionDialog = true
      this.editDetails.title = this.title
      this.editDetails.description = this.description
      this.editDetails.expiration = this.expiration
    },

    onEditCancel () {
      this.editDecisionDialog = false
    },

    onConfirmEdit () {
      // axious put request
      this.editDecisionDialog = false
      this.$router.go(0)
    },

    onConfirmDelete () {
      // axios delete request
      this.$router.go(0)
    }
  }
}
</script>
