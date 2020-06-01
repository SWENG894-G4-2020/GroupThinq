<template>
  <q-page class="column items-center justify-center bg-primary">
    <h3 class="text-grey-3">Welcome to GroupThinq!</h3>
    <q-btn
      to="/signup"
      label="Sign Up!"
      color="primary"
      size="xl"
      class="q-ma-xl bg-secondary" />
    <q-btn
      to="/login"
      label="Login!"
      color="primary"
      size="m"
      class="q-ma-xl bg-secondary" />
  </q-page>
</template>

<script>
export default {
  name: 'PageSignup'
}
</script>
