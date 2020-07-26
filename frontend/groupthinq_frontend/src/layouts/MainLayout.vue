<template>
  <q-layout view="lHh Lpr lFf">
    <q-header elevated class="q-py-lg">
      <q-toolbar>
        <q-btn
          flat
          dense
          round
          icon="menu"
          aria-label="Menu"
          @click="leftDrawerOpen = !leftDrawerOpen"
        />

        <q-toolbar-title vertical-bottom>
          <div class="row items-end">
            <div style="font-size: 1.25em">GroupThinq</div>
            <q-icon name="img:statics/img/groupthinq.svg" style="font-size: 1.5em" />
          </div>
          <div class="row items-end">
            <div class="text-weight-light" style="font-size: .6em">Make Better Decisions. Together.</div>
          </div>
        </q-toolbar-title>

        <div><UserMenu /></div>
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
          style="font-size: 1.25em"
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
import NavItem from 'src/components/NavItem'
import UserMenu from 'src/components/UserMenu'

export default {
  name: 'MainLayout',

  components: {
    NavItem,
    UserMenu
  },

  data () {
    return {
      leftDrawerOpen: false,
      navLinks: [
        {
          title: 'Home',
          caption: 'Return to the Homescreen',
          icon: 'home',
          link: '/main'
        },
        {
          title: 'Account Info',
          caption: 'View and Edit Your Account Information',
          icon: 'assignment_ind',
          link: '/main/account'
        },
        {
          title: 'Decisions',
          caption: 'View All Decisions',
          icon: 'speaker_notes',
          link: '/main/decisions'
        },
        {
          title: 'Results',
          caption: 'View All Results',
          icon: 'assignment_turned_in',
          link: '/main/results'
        },
        {
          title: 'Help',
          caption: 'GroupThinq Documentation',
          icon: 'help_outline',
          link: '/main/help'
        },
        {
          title: 'Users',
          caption: 'View All Users',
          icon: 'face',
          link: '/main/users'
        }
      ]
    }
  }
}
</script>
