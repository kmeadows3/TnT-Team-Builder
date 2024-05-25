<template>
  <div id="capstone-app">
    <ErrorBanner />
    <MainNavigation />
    <router-view />
  </div>
</template>


<script>
import MainNavigation from "./components/MainNavigation.vue";
import ErrorBanner from "./components/ErrorBanner.vue";

export default{
  components: {
    MainNavigation,
    ErrorBanner
  }
}
</script>

<style>
@import url("https://cdn.jsdelivr.net/npm/bootstrap-icons@1.3.0/font/bootstrap-icons.css");
</style>