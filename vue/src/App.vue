<template>
  <div id="capstone-app">
    <ErrorBanner />
    <h1 class="main-title">
      <span><em>"This is Not a Test"</em></span>
      <span>&nbsp;Team Builder</span>
    </h1>
    <section class="entire-page">
      <MainNavigation />
      <router-view />
    </section>
  </div>
</template>


<script>
import MainNavigation from "./components/Navigation/MainNavigation.vue";
import ErrorBanner from "./components/ErrorBanner.vue";

export default {
  components: {
    MainNavigation,
    ErrorBanner
  }
}
</script>

<style>
@import url("https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css");


* {
    box-sizing: border-box;
}

section.entire-page {
  display: flex;
}

h1.main-title {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;

  font-size: 4rem;
  margin: 0px;
  margin-top: 10px;
  border: solid 3px black;
  border-radius: 7px;
}

h1.main-title>span{
  word-wrap: unset;
  text-align: center;
}
</style>