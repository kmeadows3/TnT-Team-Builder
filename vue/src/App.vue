<template>
  <div id="capstone-app">
    <MainNavigation />
    <router-view />
  </div>
</template>


<script>
import MainNavigation from "./components/MainNavigation.vue";

export default{
  components: {
    MainNavigation
  }
}
</script>

<style>
@import url("https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css");
</style>