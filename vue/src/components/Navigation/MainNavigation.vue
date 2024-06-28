<template>
  <nav>
    <section class="flex-nav">
      <h1 class="section-title">Navigation</h1>
      <div class="flex-nav-option team-list" :class="{ selected: $store.state.currentPage == 'home' }" @click="toHome()">
        Home
      </div>
      <div class="flex-nav-option team-list" :class="{ selected: $store.state.showTeamList && $store.state.currentPage == 'dashboard' }"
        @click="showTeamListOn()" v-show="$store.state.token">
        Team List
      </div>
      <div class="flex-nav-option team-list" :class="{ selected: $store.state.currentPage == 'login' }" @click="toLogin()"
        v-show="!$store.state.token">
        Sign In
      </div>



      <TeamSelect v-if="$store.state.showTeamDetail || $store.state.showUnitDetail" />
      <UnitSelect v-if="$store.state.showUnitDetail" />
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
        this.$store.commit('SET_CURRENT_PAGE', 'dashboard')
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
  min-width: 200px;
  max-width: 200px;
  border: solid 3px black;
  border-radius: 7px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-bottom: 15px;
}

nav section.flex-nav {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

nav section.flex-nav>h1.section-title {
  font-size: 1.6rem;
}

nav section.flex-nav>h2.subsection-title {
  font-size: 1.3rem;
}

nav div.flex-nav-option {
  min-width: 80%;
  max-width: 80%;
  padding: 5px;
  border: solid 3px black;
  border-radius: 7px;
  cursor: pointer;
  text-align: center;
  box-shadow: 2px 2px 6px rgba(0, 0, 0, .2),
    2px 2px 10px rgba(0, 0, 0, .2);

}

nav div.flex-nav-option.selected {
  border: solid 3px #666;
  background-color: lightyellow;
  margin-bottom: 10px;
}


nav div.flex-nav-option.logout-button {
  margin-top: 3px;
}

nav div.flex-nav-option.team-list.selected {
  margin-bottom: 0px;
}

nav div.flex-nav-option:hover {
  transform: translateY(-1px);
  background-color: #eee;
}
</style>