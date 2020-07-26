<template>
  <q-page class="column items-center justify-center bg-primary">
    <div class="text-h3 text-grey-3 text-center">GroupThinq<q-icon name="img:statics/img/groupthinq.svg" style="font-size: 1em" /></div>
    <div class="text-h6 text-grey-3 text-weight-light text-center q-mb-md">Make better decisions. Together!</div>
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
