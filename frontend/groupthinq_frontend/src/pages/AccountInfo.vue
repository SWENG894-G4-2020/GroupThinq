<template>
  <q-page padding class="fit column items-center">
    <div class="text-h4 text-grey q-pa-lg">Account Info</div>
    <div><AccountInfoCard class="q-mx-xl"/></div>
  </q-page>
</template>

<script>
import AccountInfoCard from 'components/AccountInfoCard'
export default {
  name: 'PageAccountInfo',

  components: {
    AccountInfoCard
  }
}
</script>
