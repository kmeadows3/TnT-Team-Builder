<template>
  <div id="capstone-app">
    <ErrorBanner />
    <PopUp />
    <h1 class="main-title">
      <span><em>"This is Not a Test"</em></span>
      <span>&nbsp;Team Builder</span>
    </h1>
    <div id="absolute-button">
       <button id="view-nav" @click="toggleNav()">
        <i class="bi bi-list"></i></button>
    </div>
    <section class="entire-page">
      <MainNavigation />
      <router-view />
    </section>
  </div>
</template>


<script>
import MainNavigation from "./components/Navigation/MainNavigation.vue";
import ErrorBanner from "./components/ErrorBanner.vue";
import PopUp from "./components/PopUp/PopUp.vue";

export default {
  components: {
    MainNavigation,
    ErrorBanner,
    PopUp
  },
  methods: {
    toggleNav(){
      this.$store.commit('TOGGLE_VIEW_NAVIGATION');
    }
  }
}
</script>

<style>
@import url("https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css");
@import './assets/base.css';


* {
    box-sizing: border-box;
}

body {
  background-color: var(--page-background);
  height: 100vh -20px;
}

div#absolute-button{
  display: none;
  z-index: 99;
}

@media only screen and (max-width: 992px) {
  div#absolute-button{
    display: block;
    position: sticky;
    top: 10px;
    left: 0px;
  }

  button#view-nav{
    position: absolute;
    top: 8px;
    left: 10px;
    padding: 10px;

    border: solid var(--thick-border) var(--border-color);
    border-radius: 20px;
    background-color: var(--highlight-mid-dark);
  }
}

div#app{
  min-height: calc(100vh - 20px);
}

div#capstone-app{
  display: flex;
  flex-direction: column;
  height: 100%;
  border-radius: var(--border-radius);

}

section.entire-page {
  display: flex;
  flex-grow: 1;
}

h1.main-title {
  display: flex;
  flex-direction: column;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;

  font-size: 4em;
  margin: 0px;
  margin-top: 10px;

  border: solid var(--thick-border) var(--border-color);
  border-radius: var(--border-radius) var(--border-radius) 0px 0px;
  border-bottom: none;
  background-color: var(--standard-dark);
}

h1.main-title>span{
  color: var(--standard-medium);
  word-wrap: unset;
  text-align: center;
}

@media only screen and (max-width: 768px) {

h1.main-title {
    font-size: 3em;
}
}


</style>