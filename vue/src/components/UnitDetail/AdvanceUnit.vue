<template>
    <div>
        <div v-show="!showRandom && !showOptions">
            <button @click="pickStat()">Pick Advance</button>
            <button @click="rollRandom()">Roll Advance</button>
            <button @click="$emit('exitAdvance')">Cancel</button>
        </div>
        <div v-show="showOptions">
            <div v-show="showRandom">
                <p>You rolled: {{ randomNum }}</p>
                <p>{{ rollResult }}</p>
            </div>
            <div v-show="showPick">
                <p>You opted to pick a stat. Only do this if you've already rolled a physical die and just need to record the result.</p>
            </div>
            <div class="advance-options">
                <button class="advance-btn" @click="buyAdvance('skill')" v-show="canGainSkill">
                    Gain a Skill or Mutation</button>
                <button class="advance-btn" @click="buyAdvance('melee')" v-show="isMeleeLegal && (pickFromAllStats || rolledMeleeStrength)">
                    +1 Melee</button>
                <button class="advance-btn" @click="buyAdvance('strength')" v-show="isStrengthLegal && (pickFromAllStats || rolledMeleeStrength)">
                    +1 Strength</button>
                <button class="advance-btn" @click="buyAdvance('move')" v-show="isMoveLegal && (pickFromAllStats || rolledMoveRanged)">
                    +1 Move</button>
                <button class="advance-btn" @click="buyAdvance('ranged')" v-show="isRangedLegal && (pickFromAllStats || rolledMoveRanged)">
                    +1 Ranged</button>
                <button class="advance-btn" @click="buyAdvance('defense')" v-show="isDefenseLegal && (pickFromAllStats || rolledDefenseWound)">
                    +1 Defense</button>
                <button class="advance-btn" @click="buyAdvance('wounds')" v-show="isWoundsLegal && (pickFromAllStats || rolledDefenseWound)">
                    +1 Wound</button>
                <button class="advance-btn" @click="buyAdvance('promotion')" v-show="isPromotionLegal && canPromote">
                    Gain Promotion</button>
                <button class="advance-btn cancel" @click="clearForm()">
                    Cancel</button>
            </div>
        </div>
    </div>
</template>

<script>
import TeamsService from '../../services/TeamsService';

export default {
    data() {
        return {
            showRandom: false,
            showPick: false,
            showOptions: false,
            randomNum: '',
            baseUnit: {},
            currentRank: '',
            pickFromAllStats: false,
            canPromote: false,
            canGainSkill: false,
            rolledMeleeStrength: false,
            rolledMoveRanged: false,
            rolledDefenseWound: false,
            rollResult: ''
        }
    },
    computed: {
        isMoveLegal() {
            if ((this.$store.state.currentUnit.move >= 8)
                || (this.moveIncrease >= 1 && this.currentRank == 'Rank and File')
                || (this.moveIncrease >= 2 && this.currentRank == 'Specialist')
                || (this.moveIncrease >= 3 && this.currentRank == 'Elite')
                || (this.moveIncrease >= 4)
            ) {
                return false;
            }

            return true;
        },
        isMeleeLegal() {
            if ((this.$store.state.currentUnit.melee >= 10)
                || (this.meleeIncrease >= 1 && this.currentRank == 'Rank and File')
                || (this.meleeIncrease >= 2 && this.currentRank == 'Specialist')
                || (this.meleeIncrease >= 3 && this.currentRank == 'Elite')
                || (this.meleeIncrease >= 4)
            ) {
                return false;
            }

            return true;
        },
        isRangedLegal() {
            if ((this.$store.state.currentUnit.ranged >= 9)
                || (this.rangedIncrease >= 1 && this.currentRank == 'Rank and File')
                || (this.rangedIncrease >= 2 && this.currentRank == 'Specialist')
                || (this.rangedIncrease >= 3 && this.currentRank == 'Elite')
                || (this.rangedIncrease >= 4)
            ) {
                return false;
            }

            return true;
        },
        isStrengthLegal() {
            if ((this.$store.state.currentUnit.strength >= 12)
                || (this.$store.state.currentUnit.strength >= 10 && !this.isLargeOrHuge)
                || (this.strengthIncrease >= 1 && this.currentRank == 'Rank and File')
                || (this.strengthIncrease >= 2 && this.currentRank == 'Specialist')
                || (this.strengthIncrease >= 3 && this.currentRank == 'Elite')
                || (this.strengthIncrease >= 4)
            ) {
                return false;
            }

            return true;
        },
        isDefenseLegal() {
            if ((this.$store.state.currentUnit.defense >= 12)
                || (this.$store.state.currentUnit.defense >= 10 && !this.isLargeOrHuge)
                || (this.defenseIncrease >= 1 && this.currentRank == 'Rank and File')
                || (this.defenseIncrease >= 2 && this.currentRank == 'Specialist')
                || (this.defenseIncrease >= 3 && this.currentRank == 'Elite')
                || (this.defenseIncrease >= 4)
            ) {
                return false;
            }

            return true;
        },
        isPromotionLegal() {
            if ( this.canPromote == false
                || (this.$store.state.currentUnit.mettle >= 8)
                || (this.mettleIncrease >= 1 && this.currentRank == 'Rank and File')
                || (this.mettleIncrease >= 2 && this.currentRank == 'Specialist')
                || (this.mettleIncrease >= 3 && this.currentRank == 'Elite')
                || (this.mettleIncrease >= 4)
            ) {
                return false;
            }

            return true;
        },
        isWoundsLegal() {
            if ((this.$store.state.currentUnit.wounds >= 5)
                || (this.$store.state.currentUnit.wounds >= 4 && !this.isLargeOrHuge)
                || (this.woundsIncrease >= 0 && this.currentRank == 'Rank and File')
                || (this.woundsIncrease >= 1 && this.currentRank == 'Specialist')
                || (this.woundsIncrease >= 2 && this.currentRank == 'Elite')
                || (this.woundsIncrease >= 3)
            ) {
                return false;
            }

            return true;
        },
        allStatsMaxed() {
            return (!this.isDefenseLegal && !this.isMeleeLegal && !this.isMoveLegal && !this.isRangedLegal && !this.isStrengthLegal &&!this.isWoundsLegal)
        },
        moveIncrease() {
            return this.$store.state.currentUnit.move - this.baseUnit.move;
        },
        meleeIncrease() {
            return this.$store.state.currentUnit.melee - this.baseUnit.melee;
        },
        rangedIncrease() {
            return this.$store.state.currentUnit.ranged - this.baseUnit.ranged;
        },
        strengthIncrease() {
            return this.$store.state.currentUnit.strength - this.baseUnit.strength;
        },
        defenseIncrease() {
            return this.$store.state.currentUnit.defense - this.baseUnit.defense;
        },
        mettleIncrease() {
            return this.$store.state.currentUnit.mettle - this.baseUnit.mettle;
        },
        woundsIncrease() {
            return this.$store.state.currentUnit.wounds - this.baseUnit.wounds;
        },
        isLargeOrHuge() {
            const abilities = this.$store.state.currentUnit.skills;
            let isBig = false;
            abilities.forEach(ability => {
                isBig = (ability.name == 'Large' || ability.name == 'Huge') ? true : isBig;
            });
            return isBig;
        }
    },
    methods: {
        clearForm() {
            this.showRandom = false;
            this.showPick = false;
            this.showOptions = false;
            this.randomNum = '';
            this.pickFromAllStats = false;
            this.canGainSkill = false;
            this.canPromote = false;
            this.rolledMeleeStrength = false;
            this.rolledMoveRanged = false;
            this.rolledDefenseWound = false;
            this.rollResult = '';
        },
        pickStat() {
            this.pickFromAllStats = true;
            this.canPromote = true;
            this.canGainSkill = true;
            this.showPick = true;
            this.showOptions = true;
        },
        rollRandom() {
            this.randomNum = Math.floor(Math.random() * 10 + 1);
            this.showRandom = true;
            if (this.randomNum <= 2) {
                this.canGainSkill = true;
                this.rollResult = 'Pick a Skill (or a Mutation if the unit is a mutant).'
            } else if (this.randomNum <= 4) {
                this.pickFromMeleeStrength();
            } else if (this.randomNum <= 6) {
                this.pickFromMoveRanged();
            } else if (this.randomNum <= 8) {
                this.pickFromDefenseWound();
            } else if (this.randomNum <= 10) {
                this.canPromote = true;
                this.canGainSkill = true;
                this.pickFromAllStats = true;
                this.rollResult = 'either a promotion (unit ranks up if possible and gains +1 mettle) or any other option. If an option is missing, your unit is at the current max for that stat.';
            } else {
                console.error("WTF? You didn't roll a d10");
            }
            this.showOptions = true;
        },
        pickFromMeleeStrength(){
            this.rolledMeleeStrength = true;
                if (this.isMeleeLegal && this.isStrengthLegal){
                    this.rollResult = "Choose to gain either +1 Melee or Strength."
                } else if (this.isMeleeLegal){
                    this.rollResult = "Unit's Strength is currently maxed out, so they can only gain +1 Melee."
                } else if (this.isStrengthLegal){
                    this.rollResult = "Unit's Melee is currently maxed out, so they can only gain +1 Strength."
                } else {
                    this.dealWithMaxedStats("Melee and Strength");
                }
        },
        pickFromMoveRanged(){
            this.rolledMoveRanged = true;
                if (this.isMeleeLegal && this.isRangedLegal){
                    this.rollResult = "Choose to gain either +1 Move or Ranged."
                } else if (this.isMoveLegal){
                    this.rollResult = "Unit's Ranged is currently maxed out, so they can only gain +1 Move."
                } else if (this.isRangedLegal){
                    this.rollResult = "Unit's Move is currently maxed out, so they can only gain +1 Ranged."
                } else {
                    this.dealWithMaxedStats("Move and Ranged");
                }
        },
        pickFromDefenseWound(){
            this.rolledDefenseWound = true;
                if (this.isDefenseLegal && this.isWoundsLegal){
                    this.rollResult = "Choose to gain either +1 Wound or Defense."
                } else if (this.isDefenseLegal){
                    this.rollResult = "Unit's Wounds are currently maxed out, so they can only gain +1 Defense."
                } else if (this.isWoundsLegal){
                    this.rollResult = "Unit's Defense is currently maxed out, so they can only gain +1 Wound."
                } else {
                    this.dealWithMaxedStats("Defense and Wounds");
                }
        },
        dealWithMaxedStats(maxedStats) {
            if (!this.allStatsMaxed){
                this.pickFromAllStats = true;
                this.rollResult = "Both " + maxedStats + " are currently maxed out. Therefore, you can pick from any other stat the unit hasn't maxed."
            } else if (this.isPromotionLegal){
                this.canGainSkill = true;
                this.canPromote = true;
                this.rollResult = "Very impressive, all of the unit's stats are currently maxed out. They may either gain a Skill (or Mutation, if they're a mutant) or Promote."
            } else {
                this.canGainSkill = true;
                this.rollResult = "Incredible, all of the unit's skills are maxed and it can no longer promote. They must gain a Skill  (or Mutation, if they're a mutant)."
            }
        },
        getBaseUnit() {
            TeamsService.getBaseUnit(this.$store.state.currentUnit.unitClass)
                .then(response => this.baseUnit = response.data)
                .catch(error => console.log(error));
        },
        buyAdvance(option) {
            this.updateCurrentUnit(option);
            TeamsService.updateUnit(this.$store.state.currentUnit)
                .then(response => {
                    this.$store.commit('SET_CURRENT_UNIT', response.data);
                }).catch(error => {
                    console.error(error);
                    this.$store.dispatch('reloadCurrentUnit');
                });
        },
        updateCurrentUnit(option){
            if (option == 'skill'){
                this.$store.state.currentUnit.emptySkills++;
            } else if (option == 'melee'){
                this.$store.state.currentUnit.melee++;
            } else if (option == 'strength'){
                this.$store.state.currentUnit.strength++;
            } else if (option == 'move'){
                this.$store.state.currentUnit.move++;
            } else if (option == 'ranged'){
                this.$store.state.currentUnit.ranged++;
            } else if (option == 'defense'){
                this.$store.state.currentUnit.defense++;
            } else if (option == 'wounds'){
                this.$store.state.currentUnit.wounds++;
            } else if (option == 'promotion'){
                this.$store.state.currentUnit.mettle++;
                if (this.$store.state.currentUnit.rank == 'Rank and File'){
                    this.$store.state.currentUnit.rank = 'Specialist';
                } else if (this.$store.state.currentUnit.rank == 'Specialist'){
                    this.$store.state.currentUnit.rank = 'Elite';
                }
            } else {
                console.error("This is not a valid option");
            }
        }
    },
    created() {
        this.getBaseUnit();
        this.currentRank = this.$store.state.currentUnit.rank;
    }
}
</script>

<style>
div.advance-options {
    display: flex;
    flex-direction: column;
    align-items: center;
}

button.advance-btn {
    width: 20%;
    min-width: 150px;
}

button.cancel {
    margin-top: 10px;
}
</style>