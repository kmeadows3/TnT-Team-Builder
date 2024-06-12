<template>
    <div class="stat-container">
        <div class="stat-box">
            <p>MOVE</p>
            <p>{{calculatedMove}}{{ calculatedMove==$store.state.currentUnit.move ? "" : " (" + $store.state.currentUnit.move + ")" }}</p>
        </div>
        <div class="stat-box" v-show="!hasShield">
            <p>MELEE</p>
            <div>
                <p>{{ $store.state.currentUnit.melee }}</p>
            </div>
        </div>
        <div class="stat-box" v-show="hasShield">
            <p>MELEE(ATTACKING)</p>
            <div>
                <p>{{ $store.state.currentUnit.melee }}</p>
            </div>
        </div>
        <div class="stat-box" v-show="hasShield">
            <p>MELEE(DEFENDING)</p>
            <div>
                <p>{{$store.state.currentUnit.melee + 1}}</p>
            </div>
        </div>
        <div class="stat-box">
            <p>RANGED</p>
            <p>{{ $store.state.currentUnit.ranged }}</p>
        </div>
        <div class="stat-box">
            <p>STRENGTH</p>
            <p>{{calculatedStrength}}{{ calculatedStrength==$store.state.currentUnit.strength? "" : " (" + $store.state.currentUnit.strength + ")"}}</p>
        </div>
        <div class="stat-box">
            <p class="stat-label">DEFENSE</p>
            <div v-show="showRegularDefense">
                <p v-show="!showUnarmoredDefense">{{ $store.state.currentUnit.defense }}</p>
                <p v-show="showUnarmoredDefense">{{ calculatedRangedDefense + " (" + $store.state.currentUnit.defense + ")" }}</p>
            </div>
            <div v-show="!showRegularDefense" class="display-melee-ranged">
                <div>
                    <p class="stat-text">Melee</p>
                    <p class="stat-text">{{ calculatedMeleeDefense}}</p>
                </div>
                <div>
                    <p class="stat-text">Ranged</p>
                    <p class="stat-text">{{ calculatedRangedDefense}}</p>
                </div>
                <div>
                    <p class="stat-text">Unarmored</p>
                    <p class="stat-text">{{$store.state.currentUnit.defense}}</p>
                </div>
                
            </div>            
        </div>
    </div>
</template>

<script>

export default {
    computed: {
        showRegularDefense(){
            return     this.$store.state.currentUnit.defense == this.calculatedRangedDefense 
                    && this.$store.state.currentUnit.defense == this.calculatedMeleeDefense
                    || this.calculatedMeleeDefense == this.calculatedRangedDefense;
        },
        showUnarmoredDefense(){
            return     this.$store.state.currentUnit.defense != this.calculatedRangedDefense 
                    || this.$store.state.currentUnit.defense != this.calculatedMeleeDefense;
        },
        hasShield(){
            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped && item.shield);
            return inventory.length > 0;
        },
        calculatedMove() {

            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped);

            let reducesMovement = false
            inventory = inventory.filter((item) => {
                reducesMovement = false;
                item.itemTraits.forEach((trait) => {
                    if (trait.name == "Reduces Movement") {
                        reducesMovement = true;
                    }
                })
                return reducesMovement;
            });

            return this.$store.state.currentUnit.move - inventory.length;
        },
        calculatedRangedDefense() {
            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped && item.category == "Armor");

            let rangedBonus = 0;

            inventory.forEach( (item) => {
                if (item.rangedDefenseBonus > rangedBonus){
                    rangedBonus = item.rangedDefenseBonus;
                }               
            });
            
            return rangedBonus + this.$store.state.currentUnit.defense;
        },
        calculatedMeleeDefense() {
            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped && item.category == "Armor");

            let meleeBonus = 0;
            let hasArmor = false
            let hasShield = false;

            inventory.forEach( (item) => {
                if (item.meleeDefenseBonus > meleeBonus){
                    meleeBonus = item.meleeDefenseBonus;
                }
                if (item.shield){
                    hasShield = true;
                }
                if (!item.shield && !item.name=="Power Armor"){
                    hasArmor = true;
                }                
            });

            if (hasArmor && hasShield){
                meleeBonus++;
            }
            
            return meleeBonus + this.$store.state.currentUnit.defense;
        },
        calculatedStrength() {
            let inventory = this.$store.state.currentUnit.inventory;
            inventory = inventory.filter((item) => item.equipped && item.name == "Power Armor");

            let strength = this.$store.state.currentUnit.strength;
            if (inventory.length > 0){
                strength += 2;
            }
            return strength;
        }
    }
}

</script>

<style scoped>
div.stat-container {
    display: flex;
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}

div.stat-box {
    flex-grow: 1;
    flex-basis: auto;
    border-radius: 7px;
    margin: 7px;
}

div.display-melee-ranged{
    display: flex;
    justify-content: center;
    gap: 10px;
}

p.stat-label {
    font-weight: bold;
    margin: 10px 0px 10px 0px;
}

p.stat-text {
    margin: 0px;
}
</style>