<template>
    <div class="error-background" v-show="$store.state.showError">
        <div class="error-bar" >
        <h1>ERROR </h1>
        <h2>{{ $store.state.errorMessage }} </h2>
        <i class="bi bi-x-square" @click ="closeBar()"></i>
    </div>
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

div.error-background{
    position:fixed;
    height: 100vh;
    width: 100vw;
    margin: 0px;
    display: flex;
    top: 0px;
    left: 0px;
    z-index: 6;
    background-color: rgba(0, 0, 0, .2);
    justify-content: center;
    align-items: center;
}


div.error-bar {
    display: flex;
    flex-direction: column;
    justify-content: center;
    max-width: 70vw;
    min-width: 33vw;
    position: fixed;
    background-color: red;
    border: solid 3px black;
    border-radius: 7px;
    margin-inline: auto;
    
}

div.error-bar>h1 {
    margin: 5px;
    text-align: center;
    font-size: 3em;
}

div.error-bar>h2 {
    text-align: center;
    font-size: 2em;
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