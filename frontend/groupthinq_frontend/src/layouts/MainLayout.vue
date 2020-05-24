<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated>
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />

        <q-toolbar-title>
          GroupThinq - Make Decisions. Together.
        </q-toolbar-title>

        <div>Quasar v{{ $q.version }}</div>
      </q-toolbar>
    </q-header>

    <q-drawer
      v-model="leftDrawerOpen"
      show-if-above
      bordered
      content-class="bg-grey-1"
    >
      <q-list>
        <q-item-label
          header
          class="text-grey-8"
        >
          Navigation
        </q-item-label>
        <NavItem
          v-for="navLink in navLinks"
          :key="navLink.title"
          v-bind="navLink"
        />
      </q-list>
    </q-drawer>

    <q-page-container>
      <router-view />
    </q-page-container>
  </q-layout>
</template>

<script>
import NavItem from 'components/NavItem'

export default {
  name: 'MainLayout',

  components: {
    NavItem
  },

  data () {
    return {
      leftDrawerOpen: false,
      navLinks: [
        {
          title: 'Home',
          caption: 'Return to the Homescreen',
          icon: 'home',
          link: '/'
        },
        {
          title: 'Users',
          caption: 'View All Users',
          icon: 'face',
          link: '/users'
        },
        {
          title: 'Decisions',
          caption: 'View All Decisions',
          icon: 'speaker_notes',
          link: '/decisions'
        },
        {
          title: 'Results',
          caption: 'View All Results',
          icon: 'assignment_turned_in',
          link: '/results'
        },
        {
          title: 'Help',
          caption: 'GroupThinq Documentation',
          icon: 'help_outline',
          link: '/help'
        }
      ]
    }
  }
}
</script>
