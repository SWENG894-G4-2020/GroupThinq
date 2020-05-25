<template>
  <q-item
    clickable
    :to="link" exact
  >
    <q-item-section
      v-if="icon"
      avatar
    >
      <q-icon :name="icon" style='font-size: 3em' />
    </q-item-section>

    <q-item-section>
      <q-item-label style='font-size: 1.25em'>{{ title }}</q-item-label>
      <q-item-label caption>
        {{ caption }}
      </q-item-label>
    </q-item-section>
  </q-item>
</template>

<script>
export default {
  name: 'NavItem',
  props: {
    title: {
      type: String,
      required: true
    },

    caption: {
      type: String,
      default: ''
    },

    link: {
      type: String,
      default: '#'
    },

    icon: {
      type: String,
      default: ''
    }
  }
}
</script>
