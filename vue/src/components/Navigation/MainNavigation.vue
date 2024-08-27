<template>
  <nav :class="$store.state.viewNavigation ? 'view-nav' : ''">
    <section class="flex-nav">
      <div class="spacer" :class="{ 'visible': $store.state.viewNavigation }"></div>
      <h1 class="section-title">Navigation</h1>
      <div class="flex-nav-option team-list" :class="{ selected: $store.state.currentPage == 'home' }"
        @click="toHome()">
        Home
      </div>
      <div class="flex-nav-option team-list"
        :class="{ selected: $store.state.showTeamList && $store.state.currentPage == 'dashboard' }"
        @click="showTeamListOn()" v-show="$store.state.token">
        Team List
      </div>
      <div class="flex-nav-option team-list" :class="{ selected: $store.state.currentPage == 'login' }"
        @click="toLogin()" v-show="!$store.state.token">
        Sign In
      </div>



      <TeamSelect
        v-if="($store.state.showTeamDetail || $store.state.showUnitDetail) && $store.state.currentPage != 'login' && $store.state.currentPage != 'home'" />
      <UnitSelect
        v-if="$store.state.showUnitDetail && $store.state.currentPage != 'login' && $store.state.currentPage != 'home'" />
    </section>
    <section class="flex-nav">
      <div class="flex-nav-option logout-button" @click="logout()" v-show="$store.state.token">Logout</div>

    </section>
  </nav>
</template>


<script>
import TeamSelect from './TeamSelect.vue';
import UnitSelect from './UnitSelect.vue';

export default {
  components: {
    TeamSelect,
    UnitSelect
  },
  methods: {
    showTeamListOn() {
      if (this.$store.state.currentPage != 'dashboard') {
        this.$router.push("dashboard");
        this.$store.commit('CLEAR_CURRENT_TEAM');
        this.$store.commit('SET_CURRENT_PAGE', 'dashboard');
      } else {
        if (this.$store.state.showUnitDetail) {
          this.$store.commit('CLEAR_CURRENT_UNIT');
        }
        if (this.$store.state.showTeamDetail) {
          this.$store.commit('CLEAR_CURRENT_TEAM');
        }
      }


    },
    logout() {
      this.$router.push("logout");
    },
    toLogin() {
      this.$router.push("login");
      this.$store.commit('SET_CURRENT_PAGE', 'login');
    },
    toHome() {
      this.showTeamListOn();
      this.$router.push("/");
      this.$store.commit('SET_CURRENT_PAGE', 'home');
    }
  }
}
</script>

<style>
nav {
  min-width: 150px;
  max-width: 300px;
  width: 20%;
  border: solid var(--thick-border) var(--border-color);
  border-radius: 0 0 0 var(--border-radius);
  border-right: none;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-bottom: 15px;

  background-color: var(--standard-dark);
}

nav h1.section-title {
  border: none;
}

nav section.flex-nav {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--standard-padding);
}

nav section.flex-nav>h1.section-title {
  font-size: 1.6rem;
  background-color: var(--standard-dark);
  color: var(--title-text)
}

nav section.flex-nav>h2.subsection-title {
  color: var(--title-text);
  font-size: 1.3rem;
}

nav div.flex-nav-option {
  min-width: 80%;
  max-width: 80%;
  padding: var(--wide-padding);
  margin: 0px;

  border: solid var(--thick-border) var(--border-color);
  border-radius: var(--border-radius-button);
  cursor: pointer;
  text-align: center;
  word-wrap: break-word;

  box-shadow: 2px 2px 6px rgba(0, 0, 0, .2),
    2px 2px 10px rgba(0, 0, 0, .2);

  background-color: var(--standard-medium);
}

nav div.flex-nav-option.selected {
  background-color: var(--alternative-light);
  margin-bottom: var(--wide-padding);
}


nav div.flex-nav-option.logout-button {

  background-color: var(--highlight-mid-dark);

  &:hover {
    background-color: var(--highlight-medium);
  }

  margin-top: var(--double-wide-padding);
}

nav div.flex-nav-option.team-list.selected {
  margin-bottom: 0px;
}

nav div.flex-nav-option:hover {
  transform: translateY(-1px);
  background-color: var(--alternative-light);
}

nav h2 {
  text-align: center;
}

div.spacer {
  display: none;
}


@media only screen and (max-width: 992px) {
  nav {
    display: none;
  }

  nav.view-nav {
    display: flex;
    max-width: 100%;
    flex-grow: 1;
  }

  div.spacer {
    display: block;
    height: 43px;
  }
}
</style>