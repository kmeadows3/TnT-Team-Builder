package my.TNTBuilder.service;

import my.TNTBuilder.dao.*;
import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
import my.TNTBuilder.model.inventory.Weapon;
import my.TNTBuilder.validator.TeamValidator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class ItemServiceTests extends BaseDaoTests {

    ItemService sut;
    TeamDao teamDao;
    UnitDao unitDao;

    @Before
    public void setSut(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        teamDao = new JdbcTeamDao(jdbcTemplate);
        unitDao = new JdbcUnitDao(jdbcTemplate);
        sut = new ItemService(new JdbcItemDao(jdbcTemplate), unitDao, new TeamService(teamDao, new TeamValidator()));
    }

    @After
    public void resetItemIds(){
        ARMOR.setId(1);
        WEAPON.setId(2);
        WEAPON.setCost(5);
        ITEM.setId(3);
    }

    @Test
    public void getItemsForPurchase_returns_list_of_all_reference_items(){
        List<Item> testList = sut.getItemsForPurchase();
        ARMOR.setId(0);
        WEAPON.setId(0);
        Assert.assertEquals(10, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
        Assert.assertTrue(testList.contains(WEAPON));
    }

    @Test
    public void addItemToUnit_correctly_purchases_item_when_not_free(){
        int itemId = sut.addItemToUnit(WEAPON.getReferenceId(), 3, 1, false);
        WEAPON.setId(itemId);

        Unit testUnit = unitDao.getUnitById(3, 1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(1, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertEquals(495, testTeam.getMoney());

    }

    @Test
    public void addItemToUnit_correctly_purchases_item_when_free(){
        int itemId = sut.addItemToUnit(WEAPON.getReferenceId(), 3, 1, true);
        WEAPON.setId(itemId);

        Unit testUnit = unitDao.getUnitById(3, 1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(1, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertEquals(500, testTeam.getMoney());

    }

    @Test
    public void addItemToUnit_correctly_purchases_item_multiple_uses_not_free(){
        WEAPON.setId (sut.addItemToUnit(WEAPON.getReferenceId(), 3,1, false));
        ITEM.setId (sut.addItemToUnit(ITEM.getReferenceId(), 3,1, false));


        Unit testUnit = unitDao.getUnitById(3, 1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(2, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertTrue(testUnit.getInventory().contains(ITEM));
        Assert.assertEquals(492, testTeam.getMoney());
    }

    @Test
    public void addItemToUnit_correctly_purchases_item_multiple_uses_free(){
        WEAPON.setId (sut.addItemToUnit(WEAPON.getReferenceId(), 3,1, true));
        ITEM.setId (sut.addItemToUnit(ITEM.getReferenceId(), 3,1, true));


        Unit testUnit = unitDao.getUnitById(3, 1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(2, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertTrue(testUnit.getInventory().contains(ITEM));
        Assert.assertEquals(500, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void addItemToUnit_throws_exception_if_item_is_too_expensive_and_is_not_free(){
        sut.addItemToUnit(WEAPON.getReferenceId(), 8,4, false);
        Assert.fail();
    }

    @Test
    public void addItemToUnit_works_if_item_is_too_expensive_but_is_free(){
        WEAPON.setId(sut.addItemToUnit(WEAPON.getReferenceId(), 8,4, true));

        Unit testUnit = unitDao.getUnitById(8, 4);
        Team testTeam = teamDao.getTeamById(5,4);

        Assert.assertEquals(1, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertEquals(1, testTeam.getMoney());
    }


    @Test (expected = ServiceException.class)
    public void addItemToUnit_throws_exception_if_user_does_not_own_unit(){
        sut.addItemToUnit(WEAPON.getReferenceId(), 1,4, false);
        Assert.fail();
    }


    @Test
    public void transferItem_works_when_moving_item_from_unit_to_team() {
        sut.transferItem(1, 1,1 );
        List<Item> teamTestList = sut.itemDao.getAllItemsForTeam(1);
        List<Item> unitTestList = sut.itemDao.getAllItemsForUnit(1);

        Assert.assertEquals(4, teamTestList.size());
        Assert.assertTrue(teamTestList.contains(ARMOR));
        Assert.assertEquals(2, unitTestList.size());
        Assert.assertFalse(unitTestList.contains(ARMOR));
    }

    @Test
    public void transferItem_works_when_moving_item_from_team_to_item() {
        sut.transferItem(5, 1, 1);
        List<Item> teamTestList = sut.itemDao.getAllItemsForTeam(1);
        List<Item> unitTestList = sut.itemDao.getAllItemsForUnit(1);

        ARMOR.setId(5);
        Assert.assertEquals(2, teamTestList.size());
        Assert.assertFalse(teamTestList.contains(ARMOR));
        Assert.assertEquals(4, unitTestList.size());
        Assert.assertTrue(unitTestList.contains(ARMOR));
    }

    @Test (expected = ServiceException.class)
    public void transferItem_throws_exception_invalid_item_id() {
        sut.transferItem(99, 1, 1);
    }

    @Test (expected = ServiceException.class)
    public void transferItem_throws_exception_item_not_owned_by_team_or_unit() {
        sut.transferItem(4, 1, 1);
    }

    @Test (expected = ServiceException.class)
    public void transferItem_throws_exception_unit_not_owned_by_user() {
        sut.transferItem(4, 1, 2);
    }


}
