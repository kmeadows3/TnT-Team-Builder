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
    public int purchaseItemForTeam(int itemRefId, int teamId) {

        String sql = "INSERT INTO item(item_ref_id, team_id) VALUES (?, ?) RETURNING item_id";
        int itemId;

        try {
            itemId = jdbcTemplate.queryForObject(sql, Integer.class, itemRefId, teamId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }

        return itemId;
    }



    /*
    PRIVATE METHODS
     */
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

    private Item initializeItem(SqlRowSet row, Item item){

        item.setId(row.getInt("item_id"));
        item.setReferenceId(row.getInt("item_ref_id"));
        item.setName(row.getString("name"));
        item.setCost(row.getInt("cost"));
        item.setSpecialRules(row.getString("special_rules"));
        item.setItemTraits(getItemTraitsForItem(item.getReferenceId()));
        item.setRarity(row.getString("rarity"));
        item.setRelic(row.getBoolean("is_relic"));
        item.setHandsRequired(row.getInt("hands_required"));
        item.setCategory(row.getString("item_category"));

        return item;
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
