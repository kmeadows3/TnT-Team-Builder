package my.TNTBuilder;

import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class UnitValidatorTests {
    private final Unit UNIT_1 = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
        "Human", 50,10,5,7,6,8,6,5,0,
        "Special rules description",100,0,0,0,
         new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());

    UnitValidator sut;

    @Before
    public void setSut(){
        sut = new UnitValidator();
        UNIT_1.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        UNIT_1.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
    }


    @Test
    public void onlyNameChanged_true_if_only_name_changed(){
        Unit sameUnit = new Unit(1, 1, "UnitName2", "Trade Master", "Leader",
                "Human", 50,10,5,7,6,8,6,5,0,
                "Special rules description",100,0,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        sameUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        sameUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        boolean result = sut.onlyNameChanged(UNIT_1, sameUnit);
        Assert.assertEquals(true, result);
    }

    @Test
    public void onlyNameChanged_false_if_other_values_changed(){
        Unit sameUnit = new Unit(2, 1, "UnitName2", "Trade Master", "Leader",
                "Human", 50,10,5,7,6,8,6,5,0,
                "Special rules description",100,0,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        sameUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        sameUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        boolean result = sut.onlyNameChanged(UNIT_1, sameUnit);
        Assert.assertEquals(false, result);
    }

    @Test
    public void onlyNameChanged_false_if_names_match(){
        Unit sameUnit = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
                "Human", 50,10,5,7,6,8,6,5,0,
                "Special rules description",100,0,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        sameUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        sameUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        boolean result = sut.onlyNameChanged(UNIT_1, sameUnit);
        Assert.assertEquals(false, result);
    }

    @Test
    public void onlyUnspentExperienceGained_true_if_new_unspent_exp_is_higher_than_original(){
        Unit sameUnit = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
                "Human", 50,10,5,7,6,8,6,5,0,
                "Special rules description",100,50,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        sameUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        sameUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        boolean result = sut.onlyUnspentExpGained(UNIT_1, sameUnit);
        Assert.assertEquals(true, result);
    }

    @Test
    public void onlyUnspentExperienceGained_false_if_new_unspent_exp_is_lower_than_original(){
        Unit sameUnit = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
                "Human", 50,10,5,7,6,8,6,5,0,
                "Special rules description",100,-50,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        sameUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        sameUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        boolean result = sut.onlyUnspentExpGained(UNIT_1, sameUnit);
        Assert.assertEquals(false, result);
    }

    @Test
    public void onlyUnspentExperienceGained_false_if_new_unspent_exp_is_equal_to_original(){
        Unit sameUnit = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
                "Human", 50,10,5,7,6,8,6,5,0,
                "Special rules description",100,0,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        sameUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        sameUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        boolean result = sut.onlyUnspentExpGained(UNIT_1, sameUnit);
        Assert.assertEquals(false, result);
    }

    @Test
    public void onlyUnspentExperienceGained_false_if_other_value_changed_from_original(){
        Unit sameUnit = new Unit(1, 1, "UnitName2", "Trade Master", "Leader",
                "Human", 50,10,5,7,6,8,6,5,0,
                "Special rules description",100,50,0,0,
                new ArrayList<Skillset>(), new ArrayList<Skill>(), new ArrayList<Item>());
        sameUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));
        sameUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        boolean result = sut.onlyUnspentExpGained(UNIT_1, sameUnit);
        Assert.assertEquals(false, result);
    }

}

