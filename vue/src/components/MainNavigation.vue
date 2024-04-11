<template>
    <div id="nav">
      <router-link v-bind:to="{ name: 'home' }">Home</router-link>
      <span v-if="$store.state.token != ''">&nbsp;|&nbsp;
      <router-link v-bind:to="{ name: 'dashboard' }" @click="showTeamListOn()">Team List</router-link>&nbsp;|&nbsp;
      <router-link v-bind:to="{ name: 'logout' }">Logout</router-link></span>
    
    </div>
</template>

<script>
export default {
  methods: {
    showTeamListOn(){
      this.$store.commit('CLEAR_CURRENT_UNIT');
      this.$store.commit('CLEAR_CURRENT_TEAM');
    }
  }
}
</script>

<style scoped>
</style>