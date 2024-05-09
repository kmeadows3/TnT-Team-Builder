package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.inventory.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class JdbcItemDaoTests extends BaseDaoTests{

    private final Armor ARMOR = new Armor(1, 1, "Armor 1", 1, "N/A",
            List.of(new ItemTrait(1, "Trait 1", "Trait 1 Desc")), "N/A", false, 1,
            1, true, 2, 3, 1);
    private final Weapon WEAPON = new Weapon(2, 5, "Weapon 1", 5, "N/A",
            new ArrayList<>(), "N/A", false, 0, 5, 5, 5,
            1, "Ranged Weapon");
    private final Equipment EQUIPMENT = new Equipment(3, 3, "Equipment 1", 3, "N/A",
            new ArrayList<>(), "N/A", false, 0);

    private final Weapon RELIC_WEAPON = new Weapon(4, 6, "Relic Weapon", 6,
            "Relic Weapon Desc", new ArrayList<>(), "Rare", true, 6, 0,
            6, 6, 2, "Melee Weapon");

    private JdbcItemDao sut;

    @Before
    public void setSut(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcItemDao(jdbcTemplate);
    }

    @Test
    public void getAllItemsForUnit_returns_full_list_multiple_items(){
        List<Item> testList = sut.getAllItemsForUnit(1);
        Assert.assertEquals(3, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
        Assert.assertTrue(testList.contains(WEAPON));
        Assert.assertTrue(testList.contains(EQUIPMENT));
    }

    @Test
    public void getAllItemsForUnit_returns_full_list_single_item() {
        List<Item> testList = sut.getAllItemsForUnit(2);
        Assert.assertEquals(1, testList.size());
        Assert.assertTrue(testList.contains(RELIC_WEAPON));
    }

    @Test
    public void getAllItemsForUnit_returns_empty_list_with_no_items() {
        List<Item> testList = sut.getAllItemsForUnit(3);
        Assert.assertTrue(testList.isEmpty());
    }

    @Test
    public void getAllItemsForTeam_returns_full_list_multiple_items(){
        List<Item> testList = sut.getAllItemsForTeam(1);
        ARMOR.setId(5);
        WEAPON.setId(6);
        EQUIPMENT.setId(7);
        Assert.assertEquals(3, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
        Assert.assertTrue(testList.contains(WEAPON));
        Assert.assertTrue(testList.contains(EQUIPMENT));
    }

    @Test
    public void getAllItemsForTeam_returns_full_list_single_item() {
        RELIC_WEAPON.setId(8);
        List<Item> testList = sut.getAllItemsForTeam(2);
        Assert.assertEquals(1, testList.size());
        Assert.assertTrue(testList.contains(RELIC_WEAPON));
    }

    @Test
    public void getAllItemsForTeam_returns_empty_list_with_no_items() {
        List<Item> testList = sut.getAllItemsForTeam(3);
        Assert.assertTrue(testList.isEmpty());
    }

    @Test
    public void purchaseItemForTeam_adds_item_to_team() {
        int newId = sut.purchaseItemForTeam(1, 3);
        ARMOR.setId(newId);
        List<Item> testList = sut.getAllItemsForTeam(3);
        Assert.assertEquals(1, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
    }

    @Test (expected = DaoException.class)
    public void purchaseItemForTeam_throws_exception_invalid_item() {
        sut.purchaseItemForTeam(99, 3);
        Assert.fail();
    }

    @Test (expected = DaoException.class)
    public void purchaseItemForTeam_throws_exception_invalid_team() {
        sut.purchaseItemForTeam(1, 99);
        Assert.fail();
    }


}
