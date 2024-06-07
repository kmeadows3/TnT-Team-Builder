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
    ItemDao itemDao;

    @Before
    public void setSut(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        teamDao = new JdbcTeamDao(jdbcTemplate);
        unitDao = new JdbcUnitDao(jdbcTemplate);
        itemDao = new JdbcItemDao(jdbcTemplate);
        sut = new ItemService(itemDao, unitDao, new TeamService(teamDao, new TeamValidator()));
    }

    @After
    public void resetItemIds(){
        ARMOR.setId(1);
        WEAPON.setId(2);
        WEAPON.setCost(5);
        ITEM.setId(3);
        RELIC_WEAPON.setId(6);
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

    @Test (expected = ServiceException.class)
    public void addItemToUnit_throws_exception_if_invalid_item_ref_id(){
        sut.addItemToUnit(99, 1,1, false);
        Assert.fail();
    }

    @Test
    public void addItemToTeam_correctly_purchases_item_when_not_free(){
        int itemId = sut.addItemToTeam(RELIC_WEAPON.getReferenceId(), 1, 1, false);
        RELIC_WEAPON.setId(itemId);

        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(4, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(RELIC_WEAPON));
        Assert.assertEquals(494, testTeam.getMoney());

    }

    @Test
    public void addItemToTeam_correctly_purchases_item_when_free(){
        int itemId = sut.addItemToTeam(RELIC_WEAPON.getReferenceId(), 1, 1, true);
        RELIC_WEAPON.setId(itemId);

        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(4, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(RELIC_WEAPON));
        Assert.assertEquals(500, testTeam.getMoney());


    }

    @Test
    public void addItemToTeam_correctly_purchases_item_multiple_uses_not_free(){
        int itemId = sut.addItemToTeam(RELIC_WEAPON.getReferenceId(), 1, 1, false);
        RELIC_WEAPON.setId(itemId);
        int item2Id = sut.addItemToTeam(WEAPON.getReferenceId(), 1, 1, false);
        WEAPON.setId(item2Id);


        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(5, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(RELIC_WEAPON));
        Assert.assertTrue(testTeam.getInventory().contains(WEAPON));
        Assert.assertEquals(489, testTeam.getMoney());

    }

    @Test
    public void addItemToTeam_correctly_purchases_item_multiple_uses_free(){
        int itemId = sut.addItemToTeam(RELIC_WEAPON.getReferenceId(), 1, 1, true);
        RELIC_WEAPON.setId(itemId);
        int item2Id = sut.addItemToTeam(WEAPON.getReferenceId(), 1, 1, true);
        WEAPON.setId(item2Id);


        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(5, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(RELIC_WEAPON));
        Assert.assertTrue(testTeam.getInventory().contains(WEAPON));
        Assert.assertEquals(500, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void addItemToTeam_throws_exception_if_item_is_too_expensive_and_is_not_free(){
        sut.addItemToTeam(WEAPON.getReferenceId(), 5,4, false);
        Assert.fail();
    }

    @Test
    public void addItemToTeam_works_if_item_is_too_expensive_but_is_free(){
        WEAPON.setId(sut.addItemToTeam(WEAPON.getReferenceId(), 5,4, true));

        Team testTeam = teamDao.getTeamById(5,4);

        Assert.assertEquals(1, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(WEAPON));
        Assert.assertEquals(1, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void addItemToTeam_throws_exception_if_user_does_not_own_unit(){
        sut.addItemToTeam(WEAPON.getReferenceId(), 1,4, false);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void addItemToTeam_throws_exception_if_invalid_item_ref_id(){
        sut.addItemToTeam(99, 1,1, false);
        Assert.fail();
    }

    @Test
    public void transferItem_works_when_moving_item_from_unit_to_team() {
        sut.transferItem(1, 1,1 );
        List<Item> teamTestList = itemDao.getAllItemsForTeam(1);
        List<Item> unitTestList = itemDao.getAllItemsForUnit(1);

        Assert.assertEquals(4, teamTestList.size());
        Assert.assertTrue(teamTestList.contains(ARMOR));
        Assert.assertEquals(2, unitTestList.size());
        Assert.assertFalse(unitTestList.contains(ARMOR));
    }

    @Test
    public void transferItem_works_when_moving_item_from_team_to_item() {
        sut.transferItem(5, 1, 1);
        List<Item> teamTestList = itemDao.getAllItemsForTeam(1);
        List<Item> unitTestList = itemDao.getAllItemsForUnit(1);

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

    @Test
    public void deleteItem_deletes_item(){
        sut.deleteItem(1, 1);
        List<Item> testList = itemDao.getAllItemsForUnit(1);

        Assert.assertEquals(2, testList.size());
        Assert.assertFalse(testList.contains(ARMOR));
    }

    @Test (expected = ServiceException.class)
    public void deleteItem_throws_exception_user_does_not_own_item(){
        sut.deleteItem(1, 2);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void deleteItem_throws_exception_invalid_item_id(){
        sut.deleteItem(99, 1);
        Assert.fail();
    }

    @Test
    public void sellItem_removes_item_and_updates_money_correctly(){
        sut.sellItem(2, 1);
        List<Item> testList = itemDao.getAllItemsForUnit(1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(2, testList.size());
        Assert.assertFalse(testList.contains(WEAPON));
        Assert.assertEquals(502, testTeam.getMoney());
    }

    @Test
    public void sellItem_handles_zero_cost_items(){
        sut.sellItem(1, 1);
        List<Item> testList = itemDao.getAllItemsForUnit(1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(2, testList.size());
        Assert.assertFalse(testList.contains(ARMOR));
        Assert.assertEquals(500, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void sellItem_throws_exception_user_does_not_own_item(){
        sut.sellItem(1, 2);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void sellItem_throws_exception_invalid_item_id(){
        sut.sellItem(99, 1);
        Assert.fail();
    }


}
