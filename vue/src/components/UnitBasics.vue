<template>
    <div id="basic-information">
        <div id="name">
            <h1>{{$store.state.currentUnit.name}}</h1>
        </div>
        <div id="title">
            <p>TITLE</p>
            <p>{{ $store.state.currentUnit.unitClass }}</p>
        </div>
        <div id="rank">
            <p>RANK</p>
            <p>{{ $store.state.currentUnit.rank }}</p>
        </div>
        <div id="type">
            <p>TYPE</p>
            <p>{{ $store.state.currentUnit.species }}</p>
        </div>
        <div id="mettle">
            <p>METTLE</p>
            <p>{{ $store.state.currentUnit.mettle }}</p>
        </div>
        <div id="wounds">
            <p>WOUNDS</p>
            <p>{{ $store.state.currentUnit.wounds }}</p>
        </div>
        <div id="bs-cost">
            <p>BS COST</p>
            <p>{{ $store.state.currentUnit.bscost }}</p>
        </div>
    </div>
</template>

<style scoped>
div#basic-information{
    display: grid;
    width: 100%;
    grid-template-areas:
        "name   name   name" 
        "title  rank   type"
        "mettle wounds bs-cost";
    grid-template-columns: 1fr 1fr 1fr;
}
div#basic-information>div{
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}
div#name{
    grid-area: name;
}
div#title{
    grid-area: title;
}
div#rank{
    grid-area: rank;
}
div#type{
    grid-area: type;
}
div#mettle{
    grid-area: mettle;
}
div#wounds{
    grid-area: wounds;
}
div#bs-cost{
    grid-area: bs-cost;
}
</style>