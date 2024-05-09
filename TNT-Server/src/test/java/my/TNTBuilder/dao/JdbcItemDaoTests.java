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
        Assert.assertEquals(3, testList.size());
        Assert.assertTrue(testList.contains(TEAM_ARMOR));
        Assert.assertTrue(testList.contains(TEAM_WEAPON));
        Assert.assertTrue(testList.contains(TEAM_EQUIPMENT));
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
    public void purchaseItemForTeam_adds_item_to_team() {
        int newId = sut.purchaseItemForTeam(1, 3);
        ARMOR.setId(newId);
        List<Item> testList = sut.getAllItemsForTeam(3);
        Assert.assertEquals(1, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
        ARMOR.setId(1);
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
