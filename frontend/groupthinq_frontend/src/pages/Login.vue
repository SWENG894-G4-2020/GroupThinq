<template>
  <q-page class="flex flex-center bg-primary">
    <LoginCard />
  </q-page>
</template>

<script>
import LoginCard from 'src/components/LoginCard'
export default {
  name: 'PageLogin',

  components: {
    LoginCard
  }
}
</script>
