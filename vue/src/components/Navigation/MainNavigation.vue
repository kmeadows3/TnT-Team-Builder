<template>
  <nav>
    <div id="navigation-links">
      <router-link v-bind:to="{ name: 'home' }">Home</router-link>
      <span v-if="$store.state.token != ''">&nbsp;|&nbsp;
        <router-link v-bind:to="{ name: 'dashboard' }" @click="showTeamListOn()">Team List</router-link>&nbsp;|&nbsp;
        <router-link v-bind:to="{ name: 'logout' }">Logout</router-link></span>
    </div>
    <div id="flexible-nav" v-show="$store.state.showUnitDetail || $store.state.showTeamDetail">
      <TeamSelect v-if="$store.state.showTeamDetail || $store.state.showUnitDetail"/>
      <UnitSelect v-if="$store.state.showUnitDetail"/>
    </div>
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
      this.$store.commit('CLEAR_CURRENT_UNIT');
      this.$store.commit('CLEAR_CURRENT_TEAM');
    }
  }
}
</script>

<style>
nav{
  min-width: 200px;
  flex-basis: 15;
  flex-grow: 1;

  border: solid 3px black;
    border-radius: 7px;
}

nav>div {
  border: solid 3px black;
  border-radius: 7px;
}

nav > div#flexible-nav {
  padding-bottom: 10px;
}

nav section.flex-nav{
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
}

nav div.flex-nav-option{
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

nav div.flex-nav-option.selected{
  border: solid 3px #666;
  background-color:lightyellow;
  margin-bottom: 10px;
}

nav div.flex-nav-option:hover{
  transform: translateY(-1px);
  background-color:#eee;
}

</style>