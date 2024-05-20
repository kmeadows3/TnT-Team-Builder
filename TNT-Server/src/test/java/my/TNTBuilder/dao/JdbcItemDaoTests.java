package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.inventory.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class JdbcItemDaoTests extends BaseDaoTests{

    private JdbcItemDao sut;

    @Before
    public void setSut(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcItemDao(jdbcTemplate);
        ARMOR.setId(1);
    }

    @Test
    public void getAllItemsForUnit_returns_full_list_multiple_items(){
        List<Item> testList = sut.getAllItemsForUnit(1);
        Assert.assertEquals(3, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
        Assert.assertTrue(testList.contains(WEAPON));
        Assert.assertTrue(testList.contains(ITEM));
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
        Assert.assertEquals(3, testList.size());
        Assert.assertTrue(testList.contains(TEAM_ARMOR));
        Assert.assertTrue(testList.contains(TEAM_WEAPON));
        Assert.assertTrue(testList.contains(TEAM_ITEM));
    }

    @Test
    public void getAllItemsForTeam_returns_full_list_single_item() {
        List<Item> testList = sut.getAllItemsForTeam(2);
        Assert.assertEquals(1, testList.size());
        Assert.assertTrue(testList.contains(TEAM_RELIC_WEAPON));
    }

    @Test
    public void getAllItemsForTeam_returns_empty_list_with_no_items() {
        List<Item> testList = sut.getAllItemsForTeam(3);
        Assert.assertTrue(testList.isEmpty());
    }

    @Test
    public void addItemForTeam_adds_item_to_team() {
        int newId = sut.addItemToTeam(1, 3);
        ARMOR.setId(newId);
        List<Item> testList = sut.getAllItemsForTeam(3);
        Assert.assertEquals(1, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
        ARMOR.setId(1);
    }

    @Test (expected = DaoException.class)
    public void addItemForTeam_throws_exception_invalid_item() {
        sut.addItemToTeam(99, 3);
        Assert.fail();
    }

    @Test (expected = DaoException.class)
    public void addItemForTeam_throws_exception_invalid_team() {
        sut.addItemToTeam(1, 99);
        Assert.fail();
    }

    @Test
    public void addItemForUnit_adds_item_to_Unit() {
        int newId = sut.addItemToUnit(1, 3);
        ARMOR.setId(newId);
        List<Item> testList = sut.getAllItemsForUnit(3);
        Assert.assertEquals(1, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
    }

    @Test (expected = DaoException.class)
    public void addItemForUnit_throws_exception_invalid_item() {
        sut.addItemToUnit(99, 3);
        Assert.fail();
    }

    @Test (expected = DaoException.class)
    public void addItemForUnit_throws_exception_invalid_unit() {
        sut.addItemToUnit(1, 99);
        Assert.fail();
    }

    @Test
    public void transferItem_works_when_moving_item_from_unit_to_team() {
        sut.transferItem(1, 1, 1);
        List<Item> teamTestList = sut.getAllItemsForTeam(1);
        List<Item> unitTestList = sut.getAllItemsForUnit(1);

        Assert.assertEquals(4, teamTestList.size());
        Assert.assertTrue(teamTestList.contains(ARMOR));
        Assert.assertEquals(2, unitTestList.size());
        Assert.assertFalse(unitTestList.contains(ARMOR));
    }

    @Test
    public void transferItem_works_when_moving_item_from_team_to_item() {
        sut.transferItem(5, 1, 1);
        List<Item> teamTestList = sut.getAllItemsForTeam(1);
        List<Item> unitTestList = sut.getAllItemsForUnit(1);

        ARMOR.setId(5);
        Assert.assertEquals(2, teamTestList.size());
        Assert.assertFalse(teamTestList.contains(ARMOR));
        Assert.assertEquals(4, unitTestList.size());
        Assert.assertTrue(unitTestList.contains(ARMOR));
    }

    @Test (expected = DaoException.class)
    public void transferItem_throws_exception_invalid_item_id() {
        sut.transferItem(99, 1, 1);
    }

    @Test (expected = DaoException.class)
    public void transferItem_throws_exception_item_not_owned_by_team_or_unit() {
        sut.transferItem(4, 1, 1);
    }

    @Test
    public void deleteItem_deletes_item(){
        sut.deleteItem(1);
        List<Item> testList = sut.getAllItemsForUnit(1);

        Assert.assertEquals(2, testList.size());
        Assert.assertFalse(testList.contains(ARMOR));
    }

    @Test (expected = DaoException.class)
    public void deleteItem_throws_exception_invalid_id(){
        sut.deleteItem(99);
        Assert.fail();
    }

    @Test
    public void lookupReferenceItem_returns_reference_item(){
        Item testItem = sut.lookupReferenceItem(1);
        ARMOR.setId(0);
        Assert.assertEquals(ARMOR, testItem);
    }

    @Test (expected = DaoException.class)
    public void lookupReferenceItem_throws_exception_invalid_id(){
        sut.lookupReferenceItem(99);
        Assert.fail();
    }

}
