<template>
  <div class="home">
    <h1>Home</h1>
    <p>You must be authenticated to see this</p>
  </div>
</template>

<script>
export default {
};
</script>

<style>
div.reference {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}

div.reference>div {
    display: flex;
    text-align: left;
    gap: 20px;
    padding: 0px;
    border-bottom: solid 1px black;
}

div.reference>div:last-child{
    border-bottom: 0px;
}

div.reference>div>h3 {
    min-width: 100px;
    max-width: 100px;
    margin: 5px;
    margin-left: 20px;
    text-align: center;
    text-wrap: wrap;
}

div.reference>div>p {
    min-width: 100px;
    margin: 5px;
    text-align: left;
}

div.skillsets {
    border: solid 3px black;
    border-radius: 7px;
    text-align: center;
}
</style>
