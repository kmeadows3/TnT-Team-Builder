package my.TNTBuilder.service;

import my.TNTBuilder.dao.*;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.exception.ValidationException;
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

import java.util.ArrayList;
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
        sut = new ItemService(itemDao, unitDao, new TeamService(teamDao, new TeamValidator(), jdbcTemplate));
    }

    @After
    public void resetItemIds() throws ValidationException{
        ARMOR.setId(1);
        WEAPON.setId(2);
        WEAPON.setCost(5);
        WEAPON.setEquipped(false);
        WEAPON.setLargeCaliber(false);
        WEAPON.setHasPrefallAmmo(false);
        WEAPON.setMasterwork(false);
        ITEM.setId(3);
        TEAM_RELIC_WEAPON.setId(8);
    }

    @Test
    public void getItemsForPurchase_returns_list_of_all_reference_items() throws ServiceException{
        List<Item> testList = sut.getItemsForPurchase();
        ARMOR.setId(0);
        WEAPON.setId(0);
        Assert.assertEquals(14, testList.size());
        Assert.assertTrue(testList.contains(ARMOR));
        Assert.assertTrue(testList.contains(WEAPON));
    }

    @Test
    public void addItemToUnit_correctly_purchases_item_when_not_free() throws ServiceException{
        int itemId = sut.addItemToUnit(WEAPON.getReferenceId(), 3, 1, false);
        WEAPON.setId(itemId);

        Unit testUnit = unitDao.getUnitById(3, 1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(1, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertEquals(495, testTeam.getMoney());

    }

    @Test
    public void addItemToUnit_correctly_purchases_item_when_free() throws ServiceException{
        int itemId = sut.addItemToUnit(WEAPON.getReferenceId(), 3, 1, true);
        WEAPON.setId(itemId);

        Unit testUnit = unitDao.getUnitById(3, 1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(1, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertEquals(500, testTeam.getMoney());

    }

    @Test
    public void addItemToUnit_correctly_purchases_item_multiple_uses_not_free() throws ServiceException{
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
    public void addItemToUnit_correctly_purchases_item_multiple_uses_free() throws ServiceException{
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
    public void addItemToUnit_throws_exception_if_item_is_too_expensive_and_is_not_free() throws ServiceException{
        sut.addItemToUnit(WEAPON.getReferenceId(), 8,4, false);
        Assert.fail();
    }

    @Test
    public void addItemToUnit_works_if_item_is_too_expensive_but_is_free() throws ServiceException{
        WEAPON.setId(sut.addItemToUnit(WEAPON.getReferenceId(), 8,4, true));

        Unit testUnit = unitDao.getUnitById(8, 4);
        Team testTeam = teamDao.getTeamById(5,4);

        Assert.assertEquals(1, testUnit.getInventory().size());
        Assert.assertTrue(testUnit.getInventory().contains(WEAPON));
        Assert.assertEquals(1, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void addItemToUnit_throws_exception_if_user_does_not_own_unit() throws ServiceException{
        sut.addItemToUnit(WEAPON.getReferenceId(), 1,4, false);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void addItemToUnit_throws_exception_if_invalid_item_ref_id() throws ServiceException{
        sut.addItemToUnit(99, 1,1, false);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_new_item_is_copy_of_relic_in_team_inventory() throws ServiceException{
        sut.addItemToTeam(2, 1, 1, true);
        sut.addItemToUnit(2, 1, 1, true);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_new_item_is_copy_of_relic_in_unit_inventory() throws ServiceException{
        sut.addItemToUnit(2, 3, 1, true);
        sut.addItemToUnit(2, 1, 1, true);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_new_item_is_units_third_relic() throws ServiceException{
        try {
            sut.addItemToUnit(2, 1, 1, true);
            sut.addItemToUnit(4, 1, 1, true);
        } catch (ValidationException e){
            Assert.fail("Validation Exception occurred at wrong area.");
        }
        sut.addItemToUnit(6, 1, 1, true);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_support_weapon_unit_not_uparmed() throws ServiceException{
        sut.addItemToUnit(7, 1, 1, true);
        Assert.fail();
    }

    @Test
    public void addItemToUnit_works_with_support_weapon_if_unit_has_Uparmed() throws ServiceException{
        Item targetItem = new Weapon(0, 7, "Support Weapon", 7, "N/A",
                new ArrayList<>(), "N/A", false, 0, 0, 7, 7,
                2, "Support Weapon", false,null);

        targetItem.setId(sut.addItemToUnit(7, 3, 1, true));
        List<Item> testInventory = unitDao.getUnitById(3, 1).getInventory();
        Assert.assertTrue(testInventory.contains(targetItem));
    }

    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_unit_has_ragtag_and_item_costs_more_than_15() throws ServiceException{
        sut.addItemToUnit(11, 7, 4, true);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_unit_has_ragtag_and_item_brings_total_BS_above_15() throws ServiceException{
        try {
            sut.addItemToUnit(9, 7, 4, true);
        } catch (ValidationException e){
            Assert.fail("Validation Exception occurred at wrong area.");
        }

        sut.addItemToUnit(9, 7, 4, true);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_rank_and_file_unit_adds_relic() throws ServiceException{
        sut.addItemToUnit(2, 9, 4, true);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_freelancer_adds_relic() throws ServiceException{
        sut.addItemToUnit(2, 10, 4, true);
        Assert.fail();
    }


    @Test (expected = ValidationException.class)
    public void addItemToUnit_throws_exception_if_4th_relic_equipped_to_unit() throws ServiceException{
        try {
            sut.addItemToUnit(2, 6, 4, true);
            sut.addItemToUnit(4, 6, 4, true);
            sut.addItemToUnit(6, 5, 4, true);
        } catch (ValidationException e){
            Assert.fail("Validation Exception occurred at wrong area.");
        }
        sut.addItemToUnit(10, 5, 4, true);
        Assert.fail();
    }

    @Test
    public void addItemToTeam_correctly_purchases_item_when_not_free() throws ServiceException{
        int itemId = sut.addItemToTeam(TEAM_RELIC_WEAPON.getReferenceId(), 1, 1, false);
        TEAM_RELIC_WEAPON.setId(itemId);

        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(4, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(TEAM_RELIC_WEAPON));
        Assert.assertEquals(494, testTeam.getMoney());

    }

    @Test
    public void addItemToTeam_correctly_purchases_item_when_free() throws ServiceException{
        int itemId = sut.addItemToTeam(TEAM_RELIC_WEAPON.getReferenceId(), 1, 1, true);
        TEAM_RELIC_WEAPON.setId(itemId);

        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(4, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(TEAM_RELIC_WEAPON));
        Assert.assertEquals(500, testTeam.getMoney());


    }

    @Test
    public void addItemToTeam_correctly_purchases_item_multiple_uses_not_free() throws ServiceException{
        int itemId = sut.addItemToTeam(TEAM_RELIC_WEAPON.getReferenceId(), 1, 1, false);
        TEAM_RELIC_WEAPON.setId(itemId);
        int item2Id = sut.addItemToTeam(WEAPON.getReferenceId(), 1, 1, false);
        WEAPON.setId(item2Id);


        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(5, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(TEAM_RELIC_WEAPON));
        Assert.assertTrue(testTeam.getInventory().contains(WEAPON));
        Assert.assertEquals(489, testTeam.getMoney());

    }

    @Test
    public void addItemToTeam_correctly_purchases_item_multiple_uses_free() throws ServiceException{
        int itemId = sut.addItemToTeam(TEAM_RELIC_WEAPON.getReferenceId(), 1, 1, true);
        TEAM_RELIC_WEAPON.setId(itemId);
        int item2Id = sut.addItemToTeam(WEAPON.getReferenceId(), 1, 1, true);
        WEAPON.setId(item2Id);


        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(5, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(TEAM_RELIC_WEAPON));
        Assert.assertTrue(testTeam.getInventory().contains(WEAPON));
        Assert.assertEquals(500, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void addItemToTeam_throws_exception_if_item_is_too_expensive_and_is_not_free() throws ServiceException{
        sut.addItemToTeam(WEAPON.getReferenceId(), 5,4, false);
        Assert.fail();
    }

    @Test
    public void addItemToTeam_works_if_item_is_too_expensive_but_is_free() throws ServiceException{
        WEAPON.setId(sut.addItemToTeam(WEAPON.getReferenceId(), 5,4, true));

        Team testTeam = teamDao.getTeamById(5,4);

        Assert.assertEquals(1, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(WEAPON));
        Assert.assertEquals(1, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void addItemToTeam_throws_exception_if_user_does_not_own_unit() throws ServiceException{
        sut.addItemToTeam(WEAPON.getReferenceId(), 1,4, false);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void addItemToTeam_throws_exception_if_invalid_item_ref_id() throws ServiceException{
        sut.addItemToTeam(99, 1,1, false);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToTeam_throws_exception_if_new_item_is_copy_of_relic_in_team_inventory() throws ServiceException{
        sut.addItemToTeam(2, 1, 1, true);
        sut.addItemToTeam(2, 1, 1, true);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void addItemToTeam_throws_exception_if_new_item_is_copy_of_relic_in_unit_inventory() throws ServiceException{
        sut.addItemToUnit(2, 1, 1, true);
        sut.addItemToTeam(2, 1, 1, true);
        Assert.fail();
    }

    @Test
    public void transferItem_works_when_moving_item_from_unit_to_team()  throws ServiceException{
        sut.transferItem(1, 1,1 );
        List<Item> teamTestList = itemDao.getAllItemsForTeam(1);
        List<Item> unitTestList = itemDao.getAllItemsForUnit(1);

        Assert.assertEquals(4, teamTestList.size());
        Assert.assertTrue(teamTestList.contains(ARMOR));
        Assert.assertEquals(2, unitTestList.size());
        Assert.assertFalse(unitTestList.contains(ARMOR));
    }

    @Test
    public void transferItem_works_when_moving_item_from_team_to_Unit() throws ServiceException {
        sut.transferItem(5, 1, 1);
        List<Item> teamTestList = itemDao.getAllItemsForTeam(1);
        List<Item> unitTestList = itemDao.getAllItemsForUnit(1);

        ARMOR.setId(5);
        Assert.assertEquals(2, teamTestList.size());
        Assert.assertFalse(teamTestList.contains(ARMOR));
        Assert.assertEquals(4, unitTestList.size());
        Assert.assertTrue(unitTestList.contains(ARMOR));
    }

    @Test
    public void transferItem_works_when_moving_relic_item_from_team_to_Unit() throws ServiceException {
        sut.transferItem(9, 2, 2);
        List<Item> teamTestList = itemDao.getAllItemsForTeam(3);
        List<Item> unitTestList = itemDao.getAllItemsForUnit(2);

        TEAM_RELIC_WEAPON.setId(9);
        Assert.assertEquals(0, teamTestList.size());
        Assert.assertFalse(teamTestList.contains(ARMOR));
        Assert.assertEquals(2, unitTestList.size());
        Assert.assertTrue(unitTestList.contains(TEAM_RELIC_WEAPON));
    }

    @Test
    public void transferItem_unequips_equipped_item() throws ServiceException {
        sut.toggleEquipItem(2, 1, 1);
        sut.transferItem(2, 1, 1);
        List<Item> teamTestList = itemDao.getAllItemsForTeam(1);

        Assert.assertEquals(4, teamTestList.size());
        Assert.assertFalse(WEAPON.isEquipped());
        Assert.assertTrue(teamTestList.contains(WEAPON));

    }

    @Test (expected = ServiceException.class)
    public void transferItem_throws_exception_invalid_item_id() throws ServiceException {
        sut.transferItem(99, 1, 1);
    }

    @Test (expected = ServiceException.class)
    public void transferItem_throws_exception_item_not_owned_by_team_or_unit() throws ServiceException {
        sut.transferItem(4, 1, 1);
    }

    @Test (expected = ServiceException.class)
    public void transferItem_throws_exception_unit_not_owned_by_user() throws ServiceException {
        sut.transferItem(4, 1, 2);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void transferItem_throws_exception_unit_not_allowed_item() throws ServiceException {
        int itemId = sut.addItemToTeam(2, 7, 4, true);
        sut.transferItem(itemId, 9, 4);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void transferItem_throws_exception_freelancer_not_allowed_to_transfer_weapon() throws ServiceException {
        int itemId = sut.addItemToUnit(5, 10, 4, true);
        sut.transferItem(itemId, 10, 4);
        Assert.fail();
    }

    @Test
    public void transferItem_works_when_freelancer_transfers_armor() throws ServiceException {
        int itemId = sut.addItemToUnit(1, 10, 4, true);
        ARMOR.setId(itemId);
        sut.transferItem(itemId, 10, 4);
        Team testTeam = teamDao.getTeamById(7, 4);

        Assert.assertTrue(testTeam.getInventory().contains(ARMOR));
    }


    @Test
    public void deleteItem_deletes_item() throws ServiceException{
        sut.deleteItem(1, 1);
        List<Item> testList = itemDao.getAllItemsForUnit(1);

        Assert.assertEquals(2, testList.size());
        Assert.assertFalse(testList.contains(ARMOR));
    }

    @Test (expected = ServiceException.class)
    public void deleteItem_throws_exception_user_does_not_own_item() throws ServiceException{
        sut.deleteItem(1, 2);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void deleteItem_throws_exception_invalid_item_id() throws ServiceException{
        sut.deleteItem(99, 1);
        Assert.fail();
    }

    @Test
    public void sellItem_removes_item_and_updates_money_correctly() throws ServiceException{
        sut.sellItem(2, 1);
        List<Item> testList = itemDao.getAllItemsForUnit(1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(2, testList.size());
        Assert.assertFalse(testList.contains(WEAPON));
        Assert.assertEquals(502, testTeam.getMoney());
    }

    @Test
    public void sellItem_handles_zero_cost_items() throws ServiceException{
        sut.sellItem(1, 1);
        List<Item> testList = itemDao.getAllItemsForUnit(1);
        Team testTeam = teamDao.getTeamById(1,1);

        Assert.assertEquals(2, testList.size());
        Assert.assertFalse(testList.contains(ARMOR));
        Assert.assertEquals(500, testTeam.getMoney());
    }

    @Test (expected = ServiceException.class)
    public void sellItem_throws_exception_user_does_not_own_item() throws ServiceException{
        sut.sellItem(1, 2);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void sellItem_throws_exception_invalid_item_id() throws ServiceException{
        sut.sellItem(99, 1);
        Assert.fail();
    }

    @Test
    public void toggleEquipItem_updates_equipped_to_true() throws ServiceException {
        sut.toggleEquipItem(WEAPON.getId(), 1, 1);

        Item testWeapon = itemDao.getItemById(WEAPON.getId());

        Assert.assertTrue(testWeapon.isEquipped());
    }

    @Test
    public void toggleEquipItem_updates_equipped_to_false() throws ServiceException {

        try {
            sut.toggleEquipItem(WEAPON.getId(), 1, 1);
        } catch (ServiceException e){
            Assert.fail("Test failed during the arrange step.");
        }

        sut.toggleEquipItem(WEAPON.getId(), 1, 1);
        Item testWeapon = itemDao.getItemById(WEAPON.getId());
        Assert.assertFalse(testWeapon.isEquipped());
    }

    @Test (expected = ServiceException.class)
    public void toggleEquipItem_fails_if_user_does_not_own_unit() throws ServiceException {

        sut.toggleEquipItem(WEAPON.getId(), 1, 2);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void toggleEquipItem_fails_if_item_not_in_unit_inventory() throws ServiceException {
        sut.toggleEquipItem(WEAPON.getId(), 2, 2);
        Assert.fail();
    }

    @Test
    public void toggleEquipItem_removes_prefall_ammo_from_weapon_if_prefall_ammo_unequipped() throws ServiceException{
        int prefallAmmoId = 0;
        try {
            prefallAmmoId = sut.addItemToUnit(13, 1, 1, true);
            sut.toggleEquipItem(prefallAmmoId, 1, 1);
            WEAPON.setHasPrefallAmmo(true);
            sut.updateWeaponUpgrade(WEAPON, 1);
        }catch (ServiceException e){
            Assert.fail("Service exception thrown during arrange step");
        }

        sut.toggleEquipItem(prefallAmmoId,1, 1);
        Item testItem = itemDao.getItemById(WEAPON.getId());
        Item preFallAmmo = itemDao.getItemById(prefallAmmoId);

        Assert.assertFalse(preFallAmmo.isEquipped());
        Assert.assertFalse(((Weapon)testItem).isHasPrefallAmmo());

    }

    @Test
    public void unequipItemDuringTransfer_updates_equipped_to_false_when_equipped_starts_true() throws ServiceException {

        try {
            sut.toggleEquipItem(WEAPON.getId(), 1, 1);
        } catch (ServiceException e){
            Assert.fail("Test failed during the arrange step.");
        }

        sut.unequipItemDuringTransfer(WEAPON.getId(), 1, 1);
        Item testWeapon = itemDao.getItemById(WEAPON.getId());
        Assert.assertFalse(testWeapon.isEquipped());
    }

    @Test
    public void unequipItemDuringTransfer_does_nothing_if_equipped_starts_false() throws ServiceException {

        sut.unequipItemDuringTransfer(WEAPON.getId(), 1, 1);
        Item testWeapon = itemDao.getItemById(WEAPON.getId());
        Assert.assertFalse(testWeapon.isEquipped());
    }

    @Test (expected = ServiceException.class)
    public void unequipItemDuringTransfer_throws_exception_if_unit_not_owned_by_user() throws ServiceException {

        try {
            sut.toggleEquipItem(WEAPON.getId(), 1, 1);
        } catch (ServiceException e){
            Assert.fail("Test failed during the arrange step.");
        }

        sut.unequipItemDuringTransfer(WEAPON.getId(), 1, 2);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void unequipItemDuringTransfer_throws_exception_if_item_does_not_belong_to_unit() throws ServiceException {

        try {
            sut.toggleEquipItem(WEAPON.getId(), 1, 1);
        } catch (ServiceException e){
            Assert.fail("Test failed during the arrange step.");
        }

        sut.unequipItemDuringTransfer(WEAPON.getId(), 2, 2);
        Assert.fail();
    }

    @Test
    public void unequipItemDuringTransfer_removes_prefall_ammo_from_weapon_if_weapon_with_prefall_ammo_unequipped() throws ServiceException{
        int prefallAmmoId = 0;
        try {
            prefallAmmoId = sut.addItemToUnit(13, 1, 1, true);
            sut.toggleEquipItem(prefallAmmoId, 1, 1);
            sut.toggleEquipItem(WEAPON.getId(), 1, 1);
            WEAPON.setHasPrefallAmmo(true);
            sut.updateWeaponUpgrade(WEAPON, 1);
        }catch (ServiceException e){
            Assert.fail("Service exception thrown during arrange step");
        }

        sut.unequipItemDuringTransfer(WEAPON.getId(),1, 1);
        Item testItem = itemDao.getItemById(WEAPON.getId());

        Assert.assertFalse(((Weapon)testItem).isHasPrefallAmmo());
    }

    @Test
    public void updateWeaponUpgrade_correctly_updates_ranged_weapon_with_large_caliber() throws ServiceException{
        WEAPON.setLargeCaliber(true);
        sut.updateWeaponUpgrade(WEAPON, 1);

        Item testItem = itemDao.getItemById(WEAPON.getId());
        Team testTeam = teamDao.getTeamById(1, 1);

        Assert.assertTrue( ((Weapon)testItem).isLargeCaliber() );
        Assert.assertEquals(495, testTeam.getMoney());
    }

    @Test (expected = ValidationException.class)
    public void updateWeaponUpgrade_throws_exception_if_weapon_with_large_caliber_is_not_ranged()throws ServiceException {
        int weaponId = itemDao.addItemToUnit(14, 1);
        Weapon weapon = (Weapon) itemDao.getItemById(weaponId);
        weapon.setLargeCaliber(true);

        sut.updateWeaponUpgrade(weapon, 1);
        Assert.fail();
    }

    @Test
    public void updateWeaponUpgrade_correctly_updates_melee_weapon_with_masterwork() throws ServiceException{
        int weaponId = itemDao.addItemToUnit(14, 1);
        Weapon weapon = (Weapon) itemDao.getItemById(weaponId);
        weapon.setMasterwork(true);
        sut.updateWeaponUpgrade(weapon, 1);

        Item testItem = itemDao.getItemById(weaponId);
        Team testTeam = teamDao.getTeamById(1, 1);

        Assert.assertTrue( ((Weapon)testItem).isMasterwork() );
        Assert.assertEquals(486, testTeam.getMoney());
    }

    @Test (expected = ValidationException.class)
    public void updateWeaponUpgrade_throws_exception_if_weapon_with_masterwork_not_melee()throws ServiceException {
        WEAPON.setMasterwork(true);
        sut.updateWeaponUpgrade(WEAPON, 1);
        Assert.fail();
    }

    @Test
    public void updateWeaponUpgrade_allows_prefall_ammo_if_unit_has_prefall_ammo_equipped() throws ServiceException{

        try {
            int itemId = sut.addItemToUnit(13, 1, 1, true);
            sut.toggleEquipItem(itemId, 1, 1);
            WEAPON.setHasPrefallAmmo(true);
        }catch (ServiceException e){
            Assert.fail("Service exception thrown during arrange step");
        }

        sut.updateWeaponUpgrade(WEAPON, 1);
        Item testItem = itemDao.getItemById(WEAPON.getId());
        Assert.assertTrue( ((Weapon)testItem).isHasPrefallAmmo() );
    }

    @Test (expected = ValidationException.class)
    public void updateWeaponUpgrade_throws_exception_for_equipping_prefall_if_unit_does_not_have_prefall() throws ServiceException{
        WEAPON.setHasPrefallAmmo(true);
        sut.updateWeaponUpgrade(WEAPON, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void updateWeaponUpgrade_throws_exception_if_prefall_ammo_exists_but_not_equipped() throws ServiceException{

        try {
            int itemId = sut.addItemToUnit(13, 1, 1, true);
            WEAPON.setHasPrefallAmmo(true);
        }catch (ServiceException e){
            Assert.fail("Service exception thrown during arrange step");
        }

        sut.updateWeaponUpgrade(WEAPON, 1);
        Assert.fail();
    }

    @Test (expected = ValidationException.class)
    public void updateWeaponUpgrade_throws_exception_if_another_weapon_already_has_prefall() throws ServiceException{

        try {
            int itemId = sut.addItemToUnit(13, 1, 1, true);
            sut.toggleEquipItem(itemId, 1, 1);
            WEAPON.setHasPrefallAmmo(true);
            sut.updateWeaponUpgrade(WEAPON, 1);
        }catch (ValidationException e){
            Assert.fail("Validation exception thrown during arrange step");
        }

        int itemId = itemDao.addItemToUnit(5, 1);
        Weapon newWeapon = (Weapon)itemDao.getItemById(itemId);
        newWeapon.setHasPrefallAmmo(true);
        sut.updateWeaponUpgrade(newWeapon, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void updateWeaponUpgrade_throws_exception_if_user_does_not_own_item() throws ServiceException{
        WEAPON.setHasPrefallAmmo(true);
        sut.updateWeaponUpgrade(WEAPON, 99);
        Assert.fail();
    }

}
