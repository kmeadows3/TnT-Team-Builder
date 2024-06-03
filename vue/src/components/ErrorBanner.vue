<template>
    <div class="error-bar" v-show="$store.state.showError">
        <h1>ERROR: {{ $store.state.errorMessage }} </h1>
        <i class="bi bi-x-square" @click ="closeBar()"></i>
        
    </div>
</template>

<script>

export default {
    methods: {
        closeBar(){
            this.$store.commit('SHOW_ERROR_OFF');
        }
    }
}

</script>


<style scoped>

div.error-bar {
    display: block;
    width: 95vw;
    position: fixed;
    background-color: red;
    border: solid 3px black;
    border-radius: 7px;
    margin-inline: auto;
    left: 50%;
    transform: translate(-50%);
    
}

div.error-bar>h1 {
    margin: 5px;
    display: inline-block;
}

i.bi-x-square {
    position: absolute;
    top: 0px;
    right: 0px;
    font-weight: bold;
    font-size: 1.5em;
    margin: 5px;
    border: 0px;
}

</style>