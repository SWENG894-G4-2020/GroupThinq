<template>
  <q-page class="flex flex-center bg-primary">
    <SignUpCard />
  </q-page>
</template>

<script>
import SignUpCard from 'src/components/SignUpCard'
export default {
  name: 'PageSignup',

  components: {
    SignUpCard
  }
}
</script>
