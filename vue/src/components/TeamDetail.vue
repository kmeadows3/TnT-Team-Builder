<template>
    <section class="main-display">
        <h1 class ="page-title">{{this.$store.state.currentTeam.name}}</h1>
        <div class = "team-box">
            <div class ="team-summary">
                <div class="bigger-box">
                    <p>FACTION</p>
                    <p>{{ this.$store.state.currentTeam.faction }}</p>
                </div>
                <div class="little-box">
                    <p>BS COST</p>
                    <p>{{this.$store.state.currentTeam.bscost}}</p>
                </div>
                <div class="bigger-box">
                    <p>UNSPENT BARTER SCRIP</p>
                    <p>{{this.$store.state.currentTeam.money}}</p>
                </div>
                <div class="little-box">
                    <p>UPKEEP</p>
                    <p>{{this.$store.state.currentTeam.upkeep}}</p>
                </div>
            </div>
            <UnitList/>
        </div>
    </section>
</template>

<script>
import UnitList from './UnitList.vue';

export default {
    components: {
        UnitList
    }
}
</script>

<style scoped>

div.team-box {
    border: solid 3px black;
    border-radius: 7px;
    margin: 7px;
}

div.team-summary{
    display: flex;
    justify-content: space-between;
}

div.team-summary>div{
    text-align: center;
    flex-basis: auto;
}

div.team-summary>div.little-box{ 
    flex-grow: 1;
    min-width: 100px;
}

div.team-summary>div.bigger-box{ 
    flex-grow:2;
    min-width: 200px;
}


</style>