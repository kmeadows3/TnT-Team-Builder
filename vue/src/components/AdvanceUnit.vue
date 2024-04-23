<template>
    <div>
    <div v-show="!showRandom && !showPick">
        <button>Pick Advance</button>
        <button @click="rollRandom()">Roll Advance</button>
    </div>
    <div v-show="showPick">
        <button>Gain a Skill or Mutation</button>
        <button>+1 Melee</button>
        <button>+1 Strength</button>
        <button>+1 Move</button>
        <button>+1 Ranged</button>
        <button>+1 Defense</button>
        <button>+1 Wound</button>
        <button>Promotion</button>
    </div>
    <div v-show="showRandom">
        <p>You rolled: {{ randomNum }}</p>
    </div>
    </div>
</template>

<script>
export default {
    data() {
        return{
            showRandom: false,
            showPick: false,
            randomNum: ''
        }
    },
    methods: {
        toggleShowRandom() {
            this.showRandom = !this.showRandom;
        },
        toggleShowPick() {
            this.showPick = !this.showPick;
        },
        clearForm() {
            this.showRandom = false;
            this.showPick = false;
        },
        rollRandom() {
            this.randomNum = Math.floor(Math.random() * 10 + 1);
            this.toggleShowRandom();
        }

    }
}
</script>