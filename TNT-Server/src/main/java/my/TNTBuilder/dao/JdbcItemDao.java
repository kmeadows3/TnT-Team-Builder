package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
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

    private final String SELECT_ALL_FROM_ITEM = "SELECT item_id, i.item_ref_id, name, cost, special_rules, rarity, " +
            "is_relic, item_category, hands_required, melee_defense_bonus, ranged_defense_bonus, is_shield, " +
            "cost_2_wounds, cost_3_wounds, melee_range, ranged_range, weapon_strength, reliability " +
            "FROM item i " +
            "JOIN item_reference ir ON ir.item_ref_id = i.item_ref_id ";

    private final String SELECT_ALL_FROM_ITEM_REFERENCE = "SELECT item_ref_id, name, cost, special_rules, rarity, " +
            "is_relic, item_category, hands_required, melee_defense_bonus, ranged_defense_bonus, is_shield, " +
            "cost_2_wounds, cost_3_wounds, melee_range, ranged_range, weapon_strength, reliability " +
            "FROM item_reference ";
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
    public List<Item> getAllItemsForUnit(int unitId) {
        String sql = SELECT_ALL_FROM_ITEM + "WHERE unit_id = ? ORDER BY item_category, name";

        return getItemListFromRowSet(unitId,sql);
    }

    @Override
    public List<Item> getAllItemsForTeam(int teamId) {
        String sql = SELECT_ALL_FROM_ITEM + "WHERE team_id = ? ORDER BY item_category, name";

        return getItemListFromRowSet(teamId,sql);
    }

    @Override
    public int addItemToTeam(int itemRefId, int teamId) {

        String sql = "INSERT INTO item(item_ref_id, team_id) VALUES (?, ?) RETURNING item_id";
        return purchaseItem(itemRefId, teamId, sql);
    }

    @Override
    public int addItemToUnit(int itemRefId, int unitId) {

        String sql = "INSERT INTO item(item_ref_id, unit_id) VALUES (?, ?) RETURNING item_id";
        return purchaseItem(itemRefId, unitId, sql);
    }

    @Override
    public void transferItem(int itemId, int unitId, int teamId) {

        boolean teamToUnit = itemBelongsToTeam(itemId, teamId, unitId);
        String sql = "UPDATE item SET unit_id = ?, team_id = ? WHERE item_id = ?";

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
    public void deleteItem(int itemId){
        String sql = "DELETE FROM item WHERE item_id = ?";

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
    public Item lookupReferenceItem(int itemRefId) {
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



    /*
    PRIVATE METHODS
     */

    /**
     * This method returns true if the item belongs to the team, false if it instead belongs to the unit, and throws an
     * exception if it doesn't belong to either
     * @param itemId the id of the item
     * @param teamId the id of the team the item may belong to
     * @param unitId the id of the unit the item may belong to
     * @return if the item belongs to the team
     */
    private boolean itemBelongsToTeam(int itemId, int teamId, int unitId){
        String sql = "SELECT unit_id, team_id FROM item WHERE item_id = ?";
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

        if (teamIdOfItem == teamId){
            return true;
        } else if (unitIdOfItem == unitId){
            return false;
        }

        throw new DaoException("Invalid transfer.");

    }

    private int purchaseItem(int itemRefId, int purchaserId, String sql) {
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
    private List<Item> getItemListFromRowSet(int unitId, String sql) {
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
        }

        return itemList;
    }

    private Item mapRowToItem(SqlRowSet row) {
        Item newItem = mapItemReferenceValuesFromRow(row);
        newItem.setId(row.getInt("item_id"));
        return newItem;
    }

    private Item mapItemReferenceValuesFromRow(SqlRowSet row) {
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

    private Item initializeArmor(SqlRowSet row) {
        Armor armor = new Armor();
        initializeItem(row, armor);
        armor.setMeleeDefenseBonus(row.getInt("melee_defense_bonus"));
        armor.setRangedDefenseBonus(row.getInt("ranged_defense_bonus"));
        armor.setShield(row.getBoolean("is_shield"));
        armor.setCost2Wounds(row.getInt("cost_2_wounds"));
        armor.setCost3Wounds(row.getInt("cost_3_wounds"));
        return armor;
    }

    private Item initializeEquipment(SqlRowSet row) {
        Equipment equipment = new Equipment();
        initializeItem(row, equipment);
        return equipment;
    }

    private Item initializeWeapon(SqlRowSet row) {
        Weapon weapon = new Weapon();
        initializeItem(row, weapon);
        weapon.setMeleeRange(row.getInt("melee_range"));
        weapon.setRangedRange(row.getInt("ranged_range"));
        weapon.setStrength(row.getInt("weapon_strength"));
        weapon.setReliability(row.getInt("reliability"));
        return weapon;
    }

    private void initializeItem(SqlRowSet row, Item item){

        item.setReferenceId(row.getInt("item_ref_id"));
        item.setName(row.getString("name"));
        item.setCost(row.getInt("cost"));
        item.setSpecialRules(row.getString("special_rules"));
        item.setItemTraits(getItemTraitsForItem(item.getReferenceId()));
        item.setRarity(row.getString("rarity"));
        item.setRelic(row.getBoolean("is_relic"));
        item.setHandsRequired(row.getInt("hands_required"));
        item.setCategory(row.getString("item_category"));

    }

    private List<ItemTrait> getItemTraitsForItem(int itemRefId){
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
