<template>
  <q-page class="column items-center justify-center bg-primary">
    <div class="text-h2 text-grey-3 text-center">Welcome to GroupThinq</div>
    <div class="text-h4 text-grey-3 text-weight-thin text-center q-mb-md">Make better decisions. Together!</div>
    <div class="row justify-around">
    <q-btn
      to="/signup"
      label="Sign Up"
      color="primary"
      size="xl"
      class="bg-secondary q-mx-md" />
    <q-btn
      to="/login"
      label="Login"
      color="primary"
      size="xl"
      class="bg-secondary q-mx-md" />
    </div>
  </q-page>
</template>

<script>
export default {
  name: 'PageSignup'
}
</script>
