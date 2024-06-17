package my.TNTBuilder.model.inventory;

import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Unit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemTests {
    private static final Armor ARMOR = new Armor(1, 1, "Armor 1", 1, "N/A",
            List.of(new ItemTrait(1, "Trait 1", "Trait 1 Desc")), "N/A", false, 1,
            1, false, 2, 3, 1, "Armor", false);
    private static final Weapon WEAPON = new Weapon(2, 5, "Weapon 1", 5, "N/A",
            new ArrayList<>(), "N/A", false, 0, 5, 5, 5,
            1, "Ranged Weapon", false);
    private static final Item ITEM = new Item(3, 3, "Equipment 1", 3, "N/A",
            new ArrayList<>(), "N/A", false, 0, "Equipment", false);

    private static final Unit UNIT1 = new Unit(1, 1, "UnitName1", "Trade Master", "Leader",
            "Human", 50,10,5,7,6,8,6,5,0,
            "Special rules description",100,0,0,0,
            new ArrayList<>(Arrays.asList(new Skillset(3, "Survival", "Skill"),
                    new Skillset(4, "Quickness", "Skill"))),
            new ArrayList<>(), new ArrayList<>(Arrays.asList(ARMOR, WEAPON, ITEM)));

    private Item sut;



    @Before
    public void setSut(){
        sut = new Item();
        sut.setCategory("");
    }

    @After
    public void cleanUp() throws ValidationException{
        UNIT1.setInventory(new ArrayList<>(Arrays.asList(ARMOR, WEAPON, ITEM)));
        WEAPON.setEquipped(false);
        WEAPON.setHandsRequired(1);
        ARMOR.setEquipped(false);
        ARMOR.setShield(false);

    }


    @Test
    public void setEquippedValidated_equips_item_if_unit_has_enough_hands() {

        UNIT1.getInventory().add(sut);
        sut.setHandsRequired(1);
        try {
            sut.setEquippedValidated(true, UNIT1);
        } catch (ValidationException e) {
            Assert.fail();
        }

        Assert.assertTrue(sut.isEquipped());
    }

    @Test (expected = ValidationException.class)
    public void setEquippedValidated_throws_exception_when_unit_has_completely_full_hands() throws ValidationException{
        WEAPON.setHandsRequired(2);
        WEAPON.setEquipped(true);


        UNIT1.getInventory().add(sut);
        sut.setHandsRequired(1);

        sut.setEquippedValidated(true, UNIT1);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void setEquippedValidated_throws_exception_when_unit_does_not_have_enough_hands() throws ValidationException{
        WEAPON.setEquipped(true);

        UNIT1.getInventory().add(sut);
        sut.setHandsRequired(2);
        sut.setEquippedValidated(true, UNIT1);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void setEquippedValidated_throws_exception_when_unit_tries_to_equip_armor_while_wearing_armor_that_is_not_shield() throws ValidationException{
        ARMOR.setEquipped(true);
        ARMOR.setShield(false);

        sut = new Armor();
        sut.setCategory("Armor");
        ((Armor)sut).setShield(false);
        UNIT1.getInventory().add(sut);

        sut.setEquippedValidated(true, UNIT1);
        Assert.fail();
    }

    @Test
    public void setEquippedValidated_allows_equipping_armor_if_unit_has_shield_equipped() throws ValidationException{
        ARMOR.setEquipped(true);
        ARMOR.setShield(true);

        sut = new Armor();
        sut.setCategory("Armor");
        ((Armor)sut).setShield(false);
        UNIT1.getInventory().add(sut);

        sut.setEquippedValidated(true, UNIT1);

        Assert.assertTrue(sut.isEquipped());
    }

}
