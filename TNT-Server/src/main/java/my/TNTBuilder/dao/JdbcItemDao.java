package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.inventory.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcItemDao implements ItemDao {

    private final String SELECT_ALL_FROM_ITEM = "SELECT item_id, i.item_ref_id, is_equipped, ir.name, cost, special_rules, rarity, " +
            "is_relic, item_category, hands_required, melee_defense_bonus, ranged_defense_bonus, is_shield, " +
            "cost_2_wounds, cost_3_wounds, melee_range, ranged_range, weapon_strength, reliability, " +
            "is_masterwork, is_large_caliber, has_prefall_ammo, " +
            "sr.skill_id AS skill_id, sr.skillset_id, sr.name AS skill_name, sr.description AS skill_description, skillset_name, phase " +
            "FROM inventory i " +
            "JOIN item_reference ir ON ir.item_ref_id = i.item_ref_id " +
            "LEFT JOIN skill_reference sr ON grants = sr.skill_id " +
            "LEFT JOIN skillset_reference ssr ON  ssr.skillset_id = sr.skillset_id ";


    private final String SELECT_ALL_FROM_ITEM_REFERENCE = "SELECT item_ref_id, ir.name, cost, special_rules, rarity, " +
            "is_relic, item_category, hands_required, melee_defense_bonus, ranged_defense_bonus, is_shield, " +
            "cost_2_wounds, cost_3_wounds, melee_range, ranged_range, weapon_strength, reliability, " +
            "sr.skill_id AS skill_id, sr.skillset_id, sr.name AS skill_name, sr.description AS skill_description, skillset_name, phase " +
            "FROM item_reference ir " +
            "LEFT JOIN skill_reference sr ON grants = sr.skill_id " +
            "LEFT JOIN skillset_reference ssr ON  ssr.skillset_id = sr.skillset_id ";
    private final String SELECT_ALL_FROM_ITEM_TRAIT = "SELECT itr.item_trait_id, name, effect " +
            "FROM item_trait_reference itr " +
            "JOIN item_ref_item_trait irit ON irit.item_trait_id = itr.item_trait_id ";

    private final JdbcTemplate jdbcTemplate;

    /*
    CONSTRUCTOR
     */
    public JdbcItemDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    PUBLIC METHODS
     */

    @Override
    public List<Item> getAllItemsForUnit(int unitId) throws DaoException {
        String sql = SELECT_ALL_FROM_ITEM + "WHERE unit_id = ? ORDER BY is_equipped DESC, item_category, name, item_id";

        return getItemListFromRowSet(unitId,sql);
    }

    @Override
    public List<Item> getAllItemsForTeam(int teamId) throws DaoException {
        String sql = SELECT_ALL_FROM_ITEM + "WHERE team_id = ? ORDER BY item_category, name, item_id";

        return getItemListFromRowSet(teamId,sql);
    }

    @Override
    public int addItemToTeam(int itemRefId, int teamId) throws DaoException {

        String sql = "INSERT INTO inventory(item_ref_id, team_id) VALUES (?, ?) RETURNING item_id";
        return purchaseItem(itemRefId, teamId, sql);
    }

    @Override
    public int addItemToUnit(int itemRefId, int unitId) throws DaoException {

        String sql = "INSERT INTO inventory(item_ref_id, unit_id) VALUES (?, ?) RETURNING item_id";
        return purchaseItem(itemRefId, unitId, sql);
    }

    @Override
    public void transferItem(int itemId, int unitId, int teamId, boolean teamToUnit) throws DaoException {

        String sql = "UPDATE inventory SET unit_id = ?, team_id = ? WHERE item_id = ?";

        try {
            int rowsUpdated = 0;

            if (teamToUnit) {
                rowsUpdated = jdbcTemplate.update(sql, unitId, null, itemId);
            } else {
                rowsUpdated = jdbcTemplate.update(sql, null, teamId, itemId);
            }

            if (rowsUpdated != 1) {
                throw new DaoException("Incorrect number of rows updated");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public void deleteItem(int itemId) throws DaoException {
        String sql = "DELETE FROM inventory WHERE item_id = ?";

        try {
            int rowsDeleted = jdbcTemplate.update(sql, itemId);

            if (rowsDeleted == 0) {
                throw new DaoException("No rows deleted.");
            } else if (rowsDeleted != 1){
                throw new DaoException("Improper number of rows deleted");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public Item lookupReferenceItem(int itemRefId) throws DaoException{
        Item referenceItem = null;
        String sql = SELECT_ALL_FROM_ITEM_REFERENCE + "WHERE item_ref_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, itemRefId);
            while (results.next()){
                referenceItem = mapItemReferenceValuesFromRow(results);
            }

            if (referenceItem == null){
                throw new DaoException("Invalid reference item");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return referenceItem;
    }

    @Override
    public List<Item> getListOfItemsForPurchase() throws DaoException {
        List<Item> purchaseList = new ArrayList<>();
        String sql = SELECT_ALL_FROM_ITEM_REFERENCE;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()){
                Item newItem = mapItemReferenceValuesFromRow(results);
                purchaseList.add(newItem);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return purchaseList;
    }

    @Override
    public Item getItemById(int itemId) throws DaoException {
        String sql = SELECT_ALL_FROM_ITEM + "WHERE item_id = ?";
        Item item = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, itemId);
            while(results.next()){
                item = mapRowToItem(results);
            }

            if( item == null){
                throw new DaoException("No item with that id found in database");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (ValidationException e){
            throw new DaoException(e.getMessage(), e);
        }

        return item;
    }

    @Override
    public int getTeamIdByItemId(int itemId) throws DaoException {
        String sql = "SELECT u.team_id AS unitTeamId, t.team_id AS teamId FROM inventory i " +
                "LEFT JOIN team t ON t.team_id = i.team_id " +
                "LEFT JOIN unit u ON u.unit_id = i.unit_id " +
                "WHERE item_id = ?";
        int teamId = 0;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, itemId);
            while(results.next()){
                teamId = results.getInt("teamId") != 0 ? results.getInt("teamId") : results.getInt("unitTeamId");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        if (teamId == 0){
            throw new DaoException("Invalid item id.");
        }
        return teamId;
    }


    /**
     * This method returns true if the item belongs to the team, false if it instead belongs to the unit, and throws an
     * exception if it doesn't belong to either
     * @param itemId the id of the item
     * @param teamId the id of the team the item may belong to
     * @param unitId the id of the unit the item may belong to
     * @return if the item belongs to the team
     */
    @Override
    public boolean isItemOwnedByTeam(int itemId, int teamId, int unitId) throws ValidationException {
        int[] itemOwner = getPotentialOwnersOfItem(itemId);

        if (itemOwner[0] == teamId){
            return true;
        } else if (itemOwner[1] == unitId){
            return false;
        }

        throw new ValidationException("Invalid Transfer.");
    }

    @Override
    public void updateEquipped(Item item) throws DaoException {

        String sql = "UPDATE inventory SET is_equipped = ? WHERE item_id = ?";

        try {
            int rowsUpdated = jdbcTemplate.update(sql, item.isEquipped(), item.getId());

            if (rowsUpdated != 1) {
                throw new DaoException("Incorrect number of rows updated");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }

    @Override
    public void updateWeaponBonuses(Weapon weapon) throws DaoException {

        String sql = "UPDATE inventory SET is_masterwork = ?, is_large_caliber = ?, has_prefall_ammo = ? " +
                "WHERE item_id = ?";

        try {
            int rowsUpdated = jdbcTemplate.update(sql, weapon.isMasterwork(), weapon.isLargeCaliber(),
                    weapon.isHasPrefallAmmo(), weapon.getId());

            if (rowsUpdated != 1) {
                throw new DaoException("Incorrect number of rows updated");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

    }


    @Override
    public int getUnitIdByItemId (int itemId){
        String sql = "SELECT unit_id FROM inventory WHERE item_id = ?";
        int unitId = 0;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, itemId);
            while (results.next()){
                unitId = results.getInt("unit_id");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        if (unitId == 0){
            throw new DaoException("Item is not owned by a unit");
        }

        return unitId;
    }

    public boolean isItemOwnedByUnit(int itemId){
        int[] potentialOwners = getPotentialOwnersOfItem(itemId);
        return potentialOwners[1] != 0;
    }

    /*
    PRIVATE METHODS
     */

    private int[] getPotentialOwnersOfItem(int itemId) throws DaoException{
        String sql = "SELECT unit_id, team_id FROM inventory WHERE item_id = ?";
        int teamIdOfItem = 0;
        int unitIdOfItem = 0;

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, itemId);
            while (results.next()){
                teamIdOfItem = results.getInt("team_id");
                unitIdOfItem = results.getInt("unit_id");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return new int[]{teamIdOfItem, unitIdOfItem};
    }

    private int purchaseItem(int itemRefId, int purchaserId, String sql) throws DaoException {
        int itemId;

        try {
            itemId = jdbcTemplate.queryForObject(sql, Integer.class, itemRefId, purchaserId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return itemId;
    }
    private List<Item> getItemListFromRowSet(int unitId, String sql) throws DaoException {
        List<Item> itemList = new ArrayList<>();

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, unitId);
            while (results.next()){
                Item newItem = mapRowToItem(results);
                itemList.add(newItem);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        } catch (ValidationException e){
            throw new DaoException(e.getMessage(), e);
        }

        return itemList;
    }

    private Item mapRowToItem(SqlRowSet row) throws DaoException, ValidationException {
        Item newItem = mapItemReferenceValuesFromRow(row);
        newItem.setId(row.getInt("item_id"));
        newItem.setEquipped(row.getBoolean("is_equipped"));
        if (newItem.getClass() == Weapon.class){
            ((Weapon) newItem).setMasterwork(row.getBoolean("is_masterwork"));
            ((Weapon) newItem).setLargeCaliber(row.getBoolean("is_large_caliber"));
            ((Weapon) newItem).setHasPrefallAmmo(row.getBoolean("has_prefall_ammo"));
        }
        return newItem;
    }

    private Item mapItemReferenceValuesFromRow(SqlRowSet row) throws DaoException {
        String itemType = row.getString("item_category");
        Item newItem;

        if ( itemType.equals("Armor")) {
            newItem = initializeArmor(row);
        } else if ( itemType.equals("Equipment")) {
            newItem = initializeEquipment(row);
        } else {
            newItem = initializeWeapon(row);
        }

        return newItem;
    }

    private Skill mapRowToSkill(SqlRowSet row) {
        if (row.getInt("skill_id") != 0){
            Skill newSkill = new Skill();
            newSkill.setId(row.getInt("skill_id"));
            newSkill.setSkillsetId(row.getInt("skillset_id"));
            newSkill.setName(row.getString("skill_name"));
            newSkill.setSkillsetName(row.getString("skillset_name"));
            newSkill.setDescription(row.getString("skill_description"));
            newSkill.setPhase(row.getString("phase"));
            return newSkill;
        }
        return null;
    }

    private Item initializeArmor(SqlRowSet row) throws DaoException{
        Armor armor = new Armor();
        initializeItem(row, armor);
        armor.setMeleeDefenseBonus(row.getInt("melee_defense_bonus"));
        armor.setRangedDefenseBonus(row.getInt("ranged_defense_bonus"));
        armor.setShield(row.getBoolean("is_shield"));
        armor.setCost2Wounds(row.getInt("cost_2_wounds"));
        armor.setCost3Wounds(row.getInt("cost_3_wounds"));
        return armor;
    }

    private Item initializeEquipment(SqlRowSet row)  throws DaoException{
        Item item = new Item();
        initializeItem(row, item);
        return item;
    }

    private Item initializeWeapon(SqlRowSet row)  throws DaoException{
        Weapon weapon = new Weapon();
        initializeItem(row, weapon);
        weapon.setMeleeRange(row.getInt("melee_range"));
        weapon.setRangedRange(row.getInt("ranged_range"));
        weapon.setStrength(row.getInt("weapon_strength"));
        weapon.setReliability(row.getInt("reliability"));
        return weapon;
    }

    private void initializeItem(SqlRowSet row, Item item) throws DaoException{

        item.setReferenceId(row.getInt("item_ref_id"));
        item.setName(row.getString("name"));
        item.setCost(row.getInt("cost"));
        item.setSpecialRules(row.getString("special_rules"));
        item.setItemTraits(getItemTraitsForItem(item.getReferenceId()));
        item.setRarity(row.getString("rarity"));
        item.setRelic(row.getBoolean("is_relic"));
        item.setHandsRequired(row.getInt("hands_required"));
        item.setCategory(row.getString("item_category"));
        item.setGrants(mapRowToSkill(row));

    }

    private List<ItemTrait> getItemTraitsForItem(int itemRefId) throws DaoException{
        String sql = SELECT_ALL_FROM_ITEM_TRAIT + "WHERE item_ref_id =  ?";

        List<ItemTrait> traits = new ArrayList<>();

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, itemRefId);
            while (results.next()){
                ItemTrait itemTrait = new ItemTrait();
                itemTrait.setId(results.getInt("item_trait_id"));
                itemTrait.setName(results.getString("name"));
                itemTrait.setEffect(results.getString("effect"));
                traits.add(itemTrait);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return traits;
    }
}
