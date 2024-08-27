<template>
    <div class="error-background" v-show="$store.state.showError">
        <div class="error-bar" >
        <h1>ERROR </h1>
        <h2>{{ $store.state.errorMessage }} </h2>
        <i class="bi bi-x-lg" @click ="closeBar()"></i>
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
    background-color: rgba(0, 0, 0, .3);
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
    background-color: var(--error-color);
    border: solid var(--thick-border) var(--border-color);
    border-radius: var(--border-radius-card);
    margin-inline: auto;
    
}

div.error-bar>h1 {
    margin: var(--wide-padding);
    padding: var(--wide-padding);
    text-align: center;
    font-size: 3em;
}

div.error-bar>h2 {
    margin: 0px var(--wide-padding) 12px var(--wide-padding);
    padding: var(--wide-padding);
    text-align: center;
    font-size: 2em;
}

i.bi-x-lg {
    position: absolute;
    top: 5px;
    right: 5px;
    font-weight: bold;
    font-size: 1.2em;
    margin: 0px;
    padding: 3px 3px 0px 3px;
    border: solid var(--thick-border) var(--border-color);
    border-radius: var(--border-radius);
    box-shadow: 2px 2px 3px rgba(0, 0, 0, .2),
        2px 2px 5px rgba(0, 0, 0, .2);    
    &:hover {
        transform: translateY(-1px);
    }


}

</style>