<template>
    <div id="exp-grid" :class="{'bottom': (!$store.state.unitSkillsSorted.length && !$store.state.unitInjuriesSorted.length)} ">
        <div id="spent-exp"> <strong>SPENT EXP<span class="hideable">: </span></strong><span> {{ $store.state.currentUnit.spentExperience }}</span>
        </div>
        <div id="unspent-exp"><strong>UNSPENT EXP<span class="hideable">: </span></strong> <span>{{ $store.state.currentUnit.unspentExperience
                }}</span></div>
        <div id="advance-cost"><strong>COST TO ADVANCE<span class="hideable">: </span></strong><span> {{ $store.state.currentUnit.costToAdvance
                }}</span></div>
        <div id="unit-upkeep"><strong>UPKEEP<span class="hideable">: </span></strong><span class="last"> {{ $store.state.currentUnit.unitUpkeep }} BS</span></div>
    </div>
</template>


<style scoped>
div#exp-grid {
    display: flex;
    align-items: stretch;
    justify-content: center;
    gap: 20px;
    text-align: center;
    background-color: var(--standard-medium);
    border-top: solid var(--thin-border) var(--border-color);

    >div {
        display: flex;
        justify-content: center;

        strong {
            display:inline-block;
            padding: var(--standard-padding);
            margin: 0px;
        }

        >span {
            display: flex;
            align-items: center;
            padding: var(--standard-padding);
        }
    }

    &.bottom{
        border-radius: 0 0 var(--border-radius-card-title) var(--border-radius-card-title);
    }

}


@media only screen and (max-width: 600px) {
    div#exp-grid {
        gap: 0px;
        justify-content: space-between;

        >div {
            display: flex;
            gap: 0px;
            flex-grow: 1;

            strong {
                display: flex;
                align-items: center;
                justify-content: center;
                flex-grow: 1;

                span.hideable{
                    display: none;
                }
            }

            >span {
                flex-grow: 1;
                justify-content: center;
                background-color: var(--list-background);
                border-left: solid var(--thin-border) var(--border-color);
                border-right: solid var(--thin-border) var(--border-color);


                &.last{
                    border-right-width: 0px;
                }
            }
        }

        &.bottom div:last-child>span {
            border-radius: 0 0 var(--border-radius-card-title) 0;
        }
    }
}

@media only screen and (max-width: 475px) {
    div#exp-grid {
        font-size: .9em;
        >div {

            span {
                padding: var(--wide-padding);        
            }
            strong {
                padding: var(--standard-padding) 0px;
            }
            
        }
    }
}

@media only screen and (max-width: 350px) {
    div#exp-grid {
        >div {
            strong {
                padding: var(--standard-padding) 0px;
                word-break: break-all;
                
            }
            
        }
    }
}
</style>