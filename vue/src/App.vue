<template>
  <div id="capstone-app">
    <ErrorBanner />
    <PopUp />
    <div id="absolute-button">
       <button id="view-nav" @click="toggleNav()">{{this.$store.state.viewNavigation ? 'Close Nav' : 'Open Nav' }}</button>
    </div>
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


* {
    box-sizing: border-box;
}

div#absolute-button{
  display: none;
}

@media only screen and (max-width: 992px) {
  div#absolute-button{
    display: block;
    position: sticky;
    top: 20px;
  }

  button#view-nav{
    position: absolute;
    left: 10px;
    
  }
}


section.entire-page {
  display: flex;
}

h1.main-title {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  justify-content: center;

  font-size: 4em;
  margin: 0px;
  margin-top: 10px;
  border: solid 3px black;
  border-radius: 7px;
}

h1.main-title>span{
  word-wrap: unset;
  text-align: center;
}

@media only screen and (max-width: 768px) {

h1.main-title {
    font-size: 3em;
}
}


</style>