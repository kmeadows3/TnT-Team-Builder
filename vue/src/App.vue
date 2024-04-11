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