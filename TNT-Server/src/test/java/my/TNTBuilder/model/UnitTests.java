package my.TNTBuilder.model;

import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.inventory.Armor;
import my.TNTBuilder.model.inventory.ItemTrait;
import my.TNTBuilder.model.inventory.Weapon;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitTests {


    protected static final Armor ARMOR = new Armor(1, 1, "Armor 1", 1, "N/A",
            List.of(new ItemTrait(1, "Trait 1", "Trait 1 Desc")), "N/A", false, 1,
            1, true, 2, 3, 1, "Armor", false);
    protected static final Weapon WEAPON = new Weapon(2, 5, "Weapon 1", 5, "N/A",
            new ArrayList<>(), "N/A", false, 0, 5, 5, 5,
            1, "Ranged Weapon", false);

    protected static final Weapon RELIC_WEAPON = new Weapon(4, 6, "Relic Weapon", 6,
            "Relic Weapon Desc", new ArrayList<>(), "Rare", true, 6, 0,
            6, 6, 2, "Melee Weapon", false);


    private Unit unit;


    @Before
    public void setUnit() {
        unit = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
                "Human", 50,10,5,7,6,8,6,5,0,
                "Special rules description",100,0,0,0,
                Arrays.asList(new Skillset(3, "Survival", "Skill"),
                        new Skillset(4, "Quickness", "Skill")),
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }

    @Test
    public void getBSCost_calculates_correctly_no_items_no_advances(){
          Assert.assertEquals(50, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_no_items_one_advance(){
        unit.setTotalAdvances(1);
        Assert.assertEquals(55, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_no_items_multiple_advances(){
        unit.setTotalAdvances(3);
        Assert.assertEquals(65, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_no_items_multiple_advances_ten_point_advance(){
        unit.setTotalAdvances(3);
        unit.setTenPointAdvances(1);
        Assert.assertEquals(70, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_one_item_no_advance(){
        unit.getInventory().add(WEAPON);
        Assert.assertEquals(55, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_multiple_items_no_advance(){
        unit.getInventory().add(WEAPON);
        unit.getInventory().add(WEAPON);
        Assert.assertEquals(60, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_items_and_advances(){
        unit.getInventory().add(WEAPON);
        unit.getInventory().add(WEAPON);
        unit.setTotalAdvances(2);
        unit.setTenPointAdvances(1);
        Assert.assertEquals(75, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_one_armor_3_plus_wounds(){
        unit.getInventory().add(ARMOR);
        Assert.assertEquals(53, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_one_armor_2_wounds(){
        unit.setWounds(2);
        unit.getInventory().add(ARMOR);
        Assert.assertEquals(52, unit.getBSCost());
    }

    @Test
    public void getBSCost_calculates_correctly_one_armor_1_wound(){
        unit.setWounds(1);
        unit.getInventory().add(ARMOR);
        Assert.assertEquals(51, unit.getBSCost());
    }

    @Test
    public void getUnitUpkeep_calculates_correctly_leader_no_relics(){
        Assert.assertEquals(3, unit.getUnitUpkeep());
    }

    @Test
    public void getUnitUpkeep_calculates_correctly_specialist_no_relics(){
        unit.setRank("Specialist");
        Assert.assertEquals(2, unit.getUnitUpkeep());
    }

    @Test
    public void getUnitUpkeep_calculates_correctly_elite_no_relics(){
        unit.setRank("Elite");
        Assert.assertEquals(2, unit.getUnitUpkeep());
    }

    @Test
    public void getUnitUpkeep_calculates_correctly_rank_and_file_no_relics(){
        unit.setRank("Rank and File");
        Assert.assertEquals(1, unit.getUnitUpkeep());
    }

    @Test
    public void getUnitUpkeep_calculates_correctly_scavenger_skill(){
        unit.getSkills().add(new Skill(1, "Scavenger", "", 1, ""));
        Assert.assertEquals(0, unit.getUnitUpkeep());
    }

    @Test
    public void getUnitUpkeep_calculates_correctly_leader_1_relic(){
        unit.getInventory().add(RELIC_WEAPON);
        Assert.assertEquals(4, unit.getUnitUpkeep());
    }

    @Test
    public void getUnitUpkeep_calculates_correctly_leader_multiple_relics(){
        unit.getInventory().add(RELIC_WEAPON);
        unit.getInventory().add(RELIC_WEAPON);
        unit.getInventory().add(RELIC_WEAPON);
        Assert.assertEquals(6, unit.getUnitUpkeep());
    }

    @Test
    public void getCostToAdvance_calculates_correctly(){
        unit.setSpentExperience(20);
        Assert.assertEquals(5, unit.getCostToAdvance());
        unit.setSpentExperience(45);
        Assert.assertEquals(6, unit.getCostToAdvance());
        unit.setSpentExperience(74);
        Assert.assertEquals(7, unit.getCostToAdvance());
        unit.setSpentExperience(75);
        Assert.assertEquals(8, unit.getCostToAdvance());
    }

    @Test (expected = ValidationException.class)
    public void setMettle_throws_exception_if_value_is_zero() throws ValidationException{
        unit.setMettle(0);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void setMove_throws_exception_if_value_is_zero() throws ValidationException{
        unit.setMove(0);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void setDefense_throws_exception_if_value_is_zero() throws ValidationException{
        unit.setDefense(0);
        Assert.fail();
    }



}
