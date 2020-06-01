<template>
  <q-page padding class="column items-center justify-center">
    <div class="text-h4 text-grey q-pa-lg">Account Info</div>
    <AccountInfoCard style="min-width: 50%" />
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
