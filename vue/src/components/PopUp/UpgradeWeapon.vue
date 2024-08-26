<template>
    <div>
        <h1 class="section-title popup">Upgrade {{upgradedWeapon.name}}</h1>
        <div class="upgrade-container">
            <p class="center">Select upgrade from below:</p>
            <p class="center"><strong>Available BS: </strong>{{ $store.state.currentTeam.money }}</p>
            <div class="option-list">
                <p v-show="upgradedWeapon.category == 'Melee Weapon' && !upgradedWeapon.masterwork">
                    <span class="button" @click="setMasterwork()"><button>Masterwork</button></span>
                    <span class="cost">(Cost: {{ upgradedWeapon.cost }} BS)</span>
                    <span>Weapon gains the Balanced trait, giving it +1 to melee attacks.</span>
                </p>
                <p v-show="upgradedWeapon.category == 'Ranged Weapon' && !upgradedWeapon.largeCaliber">
                    <span class="button" @click="setLargeCaliber()"><button>Large Caliber</button></span>
                    <span class="cost">(Cost: {{ upgradedWeapon.cost }} BS)</span>
                    <span>Weapon upgraded to use large caliber rounds, granting +1 to damage.</span>
                </p>
                <p v-show="upgradedWeapon.category == 'Ranged Weapon' && canHaveAmmo">            
                    <span class="button" @click="setHasPrefallAmmo()"><button>{{upgradedWeapon.hasPrefallAmmo ? 'Remove' : 'Equip'}} Pre-Fall Ammo</button></span>
                    <span class="cost">(Cost: Free)</span>
                    <span>Equip pre-fall ammo on this weapon.</span>
                </p>
            </div>
            
            <span class="popup-buttons">
                <button @click="cancel()" class="danger">Cancel</button>
            </span>
        </div>



    </div>
</template>

<script>
import ItemService from '../../services/ItemService';

export default {
    data() {
        return {
            upgradedWeapon: ''
        }
    },
    computed: {
        canHaveAmmo() {
            let inventory = this.$store.state.currentUnit.inventory;
            let inventoryHasPrefall = false;
            let preFallEquipped = false;
            inventory.forEach( inventoryItem => {
                if (inventoryItem.hasPrefallAmmo && inventoryItem.id != this.upgradedWeapon.id){
                    preFallEquipped = true;
                } else if (inventoryItem.name == 'Pre-Fall Ammo'){
                    inventoryHasPrefall = true;
                }
            });

            preFallEquipped ? inventoryHasPrefall = false :  inventoryHasPrefall;
            return inventoryHasPrefall;
        }
    },
    methods: {
        getWeapon() {
            this.upgradedWeapon = this.$store.state.itemToModify;
        },
        setMasterwork() {
            this.upgradedWeapon.masterwork = true;
            this.upgradeWeapon();
        },
        setLargeCaliber() {
            this.upgradedWeapon.largeCaliber = true;
            this.upgradeWeapon();
        },
        setHasPrefallAmmo() {
            this.upgradedWeapon.hasPrefallAmmo = !this.upgradedWeapon.hasPrefallAmmo;
            this.upgradeWeapon();
        },
        upgradeWeapon() {
            ItemService.upgradeItem(this.upgradedWeapon)
                .then(response => {
                    this.$store.dispatch('reloadCurrentUnit');
                    this.cancel();
                })
                .catch(error => {
                    this.$store.dispatch('showError', error);
                    this.cancel();
                });
        },
        cancel() {
            this.upgradedWeapon = '';
            this.$store.commit('SET_ITEM_TO_MODIFY', '');
            this.$store.commit('REMOVE_SHOW_POPUP');
        }
    },
    created() {
        this.getWeapon();
    }
}
</script>


<style scoped>
div.upgrade-container{
    padding: var(--standard-padding);
}

p {
    margin: 0px;
    padding: var(--standard-padding) 0px;
    &.center{
        text-align: center;
    }
}

div.option-list>p{
    display: flex;
    align-items:center;
    gap: var(--wide-padding);
}

div.option-list>p>span.cost {
    font-style: italic;
}

div.option-list button {
    width: 150px;
}

</style>