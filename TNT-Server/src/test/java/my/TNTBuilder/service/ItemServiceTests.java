package my.TNTBuilder.service;

import my.TNTBuilder.dao.*;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.model.Unit;
import my.TNTBuilder.model.inventory.Item;
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
    public void purchaseItemForUnit_correctly_purchases_item(){
        int itemId = sut.purchaseItemForUnit(WEAPON.getReferenceId(), 3, 1);
        WEAPON.setId(itemId);

        Unit testUnit = unitDao.getUnitById(3, 1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(1, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertEquals(495, testTeam.getMoney());

    }

    @Test
    public void purchaseItemForUnit_correctly_purchases_item_multiple_uses(){
        WEAPON.setId (sut.purchaseItemForUnit(WEAPON.getReferenceId(), 3,1));
        ITEM.setId (sut.purchaseItemForUnit(ITEM.getReferenceId(), 3,1));


        Unit testUnit = unitDao.getUnitById(3, 1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(2, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertTrue(testUnit.getInventory().contains(ITEM));
        Assert.assertEquals(492, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void purchaseItemForUnit_throws_exception_if_item_is_too_expensive(){
        sut.purchaseItemForUnit(WEAPON.getReferenceId(), 8,4);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void purchaseItemForUnit_throws_exception_if_user_does_not_own_unit(){
        sut.purchaseItemForUnit(WEAPON.getReferenceId(), 1,4);
        Assert.fail();
    }



}
