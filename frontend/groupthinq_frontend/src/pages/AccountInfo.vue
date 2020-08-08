<template>
  <q-page padding class="row justify-center">
    <AccountInfoCard class="col-xs-12 col-md-8 col-lg-6"/>
  </q-page>
</template>

<script>
import AccountInfoCard from 'src/components/AccountInfoCard'
export default {
  name: 'PageAccountInfo',

  components: {
    AccountInfoCard
  }
}
</script>
