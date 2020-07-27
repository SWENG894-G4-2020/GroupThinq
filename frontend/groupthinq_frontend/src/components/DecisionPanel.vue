<template>
  <div class="q-pa-md full-width">
  </div>
</template>

<script>
import auth from 'src/store/auth'

export default {
  name: 'DecisionPanel',

  data () {
    return {
      currentUserName: '',
      decisionId: '',
      mode: 'view',
      modeData: {
        view: {

        },
        edit: {

        },
        create: {

        }
      }
    }
  },

  mounted () {
    this.currentUserName = auth.getTokenData().sub
  }
}
</script>
