package my.TNTBuilder.model;

import my.TNTBuilder.model.inventory.Armor;
import my.TNTBuilder.model.inventory.ItemTrait;
import my.TNTBuilder.model.inventory.Weapon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TeamTests {

    private static final Armor ARMOR = new Armor(1, 1, "Armor 1", 1, "N/A",
            List.of(new ItemTrait(1, "Trait 1", "Trait 1 Desc")), "N/A", false, 1,
            1, true, 2, 3, 1, "Armor", false,0);
    private static final Weapon WEAPON = new Weapon(2, 5, "Weapon 1", 5, "N/A",
            new ArrayList<>(), "N/A", false, 0, 5, 5, 5,
            1, "Ranged Weapon", false,0);

    private static final Weapon RELIC_WEAPON = new Weapon(4, 6, "Relic Weapon", 6,
            "Relic Weapon Desc", new ArrayList<>(), "Rare", true, 6, 0,
            6, 6, 2, "Melee Weapon", false,0);

    private static final Unit UNIT1 = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
            "Human", 50,10,5,7,6,8,6,5,0,
            "Special rules description",100,0,0,0,
            new ArrayList<>(), new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());
    private static final Unit UNIT2 = new Unit(2, 3, "UnitName2", "Soldier", "Elite",
            "Mutant", 51,11,6,8,7,9,7,6,1,
            "Special rules description",50,0,0,0,
            new ArrayList<>(), new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());
    private Team team;

    @Before
    public void setTeam() {
        team = new Team(1, 1, "Team 1", "Caravanners", 1, 500, new ArrayList<>(),
                new ArrayList<>());
        UNIT1.setInventory(new ArrayList<>());
    }

    @Test
    public void getBSCost_returns_zero_no_units_empty_inventory(){
        Assert.assertEquals(0, team.getBSCost());
    }

    @Test
    public void getBSCost_returns_zero_no_units_no_relics(){
        team.getInventory().add(ARMOR);
        team.getInventory().add(WEAPON);
        Assert.assertEquals(0, team.getBSCost());
    }

    @Test
    public void getBSCost_returns_correctly_no_units_one_relic(){
        team.getInventory().add(RELIC_WEAPON);
        Assert.assertEquals(6, team.getBSCost());
    }

    @Test
    public void getBSCost_returns_correctly_no_units_multiple_relics(){
        team.getInventory().add(RELIC_WEAPON);
        team.getInventory().add(RELIC_WEAPON);
        team.getInventory().add(RELIC_WEAPON);
        Assert.assertEquals(18, team.getBSCost());
    }

    @Test
    public void getBSCost_returns_correctly_1_unit_no_inventory(){
        team.getUnitList().add(UNIT1);
        Assert.assertEquals(50, team.getBSCost());
    }

    @Test
    public void getBSCost_returns_correctly_multiple_units_no_inventory(){
        team.getUnitList().add(UNIT1);
        team.getUnitList().add(UNIT2);
        Assert.assertEquals(101, team.getBSCost());
    }

    @Test
    public void getRelicCount_returns_zero_no_relics(){
        Assert.assertEquals(0, team.getRelicCount());
    }

    @Test
    public void getRelicCount_returns_correct_number_no_unit_relics(){
        team.getInventory().addAll(Arrays.asList(RELIC_WEAPON, RELIC_WEAPON, RELIC_WEAPON));
        Assert.assertEquals(3, team.getRelicCount());
    }

    @Test
    public void getRelicCount_returns_correct_number_only_unit_relics(){
        UNIT1.getInventory().add(RELIC_WEAPON);
        team.getUnitList().add(UNIT1);
        Assert.assertEquals(1, team.getRelicCount());
    }

    @Test
    public void getRelicCount_returns_correct_number_both_unit_relics_and_inventory_relics(){
        team.getInventory().addAll(Arrays.asList(RELIC_WEAPON, RELIC_WEAPON, RELIC_WEAPON));
        UNIT1.getInventory().add(RELIC_WEAPON);
        team.getUnitList().add(UNIT1);
        Assert.assertEquals(4, team.getRelicCount());
    }

    @Test
    public void getUpkeep_returns_zero_no_units_no_inventory(){
        Assert.assertEquals(0, team.getUpkeep());
    }

    @Test
    public void getUpkeep_returns_correctly_1_unit_no_inventory(){
        team.getUnitList().add(UNIT1);
        Assert.assertEquals(3, team.getUpkeep());
    }

    @Test
    public void getUpkeep_returns_correctly_multiple_units_no_inventory(){
        team.getUnitList().add(UNIT1);
        team.getUnitList().add(UNIT2);
        Assert.assertEquals(5, team.getUpkeep());
    }

    @Test
    public void getUpkeep_returns_correctly_no_units_no_relics(){
        team.getInventory().addAll(Arrays.asList(ARMOR, WEAPON));
        Assert.assertEquals(0, team.getUpkeep());
    }

    @Test
    public void getUpkeep_returns_correctly_no_units_one_relic(){
        team.getInventory().add(RELIC_WEAPON);
        Assert.assertEquals(1, team.getUpkeep());
    }

    @Test
    public void getUpkeep_returns_correctly_no_units_multiple_relics(){
        team.getInventory().addAll(Arrays.asList(RELIC_WEAPON, RELIC_WEAPON, RELIC_WEAPON));
        Assert.assertEquals(3, team.getUpkeep());
    }



}
