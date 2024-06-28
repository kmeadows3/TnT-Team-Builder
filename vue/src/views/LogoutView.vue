<template>
    <section class="logout main-display">

  <h1>Logout</h1>
    </section>
</template>

<script>
export default {
  created() {
    this.$store.commit("LOGOUT");
    this.$router.push("/");
    this.$store.commit('SET_CURRENT_PAGE', 'home')
  }
};
</script>
