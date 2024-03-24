package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Unit;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JdbcUnitDao implements UnitDao{
    private final JdbcTemplate jdbcTemplate;
    private final String SELECT_ALL_FROM_UNIT_REFERENCE = "SELECT unit_ref_id, faction_id, class, rank, species, base_cost, " +
            "wounds, defense, mettle, move, ranged, melee, strength, skillsets, starting_skills, starting_free_skills, " +
            "special_rules FROM unit_reference ";
    private final String SELECT_ALL_FROM_UNIT = "SELECT unit_id, name, class, rank, species, base_cost, wounds, " +
            "defense, mettle, move, ranged, melee, strength, empty_skills, special_rules, spent_exp, unspent_exp, " +
            "total_advances, ten_point_advances, u.team_id FROM unit u " +
            "JOIN team t on t.team_id = u.team_id ";
    private final String SELECT_ALL_FROM_SKILLSET_REFERENCE = "SELECT skillset_id, skillset_name, category " +
            "FROM skillset_reference ";
    private final String SELECT_ALL_FROM_SKILL_REFERENCE = "SELECT skill_id, sr.skillset_id, name, description, " +
            "skillset_name FROM skill_reference sr " +
            "JOIN skillset_reference ssr ON ssr.skillset_id = sr.skillset_id ";

    public JdbcUnitDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Unit getUnitById(int id) {
        //TODO secure this later
        String sql = SELECT_ALL_FROM_UNIT + "WHERE unit_id = ?";
        Unit unit = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, Unit.class);
            while (results.next()){
                unit = mapRowToUnit(results);
            }
            if (unit == null){
                throw new DaoException("Unit cannot be retrieved from database");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }

        return unit;
    }

    @Override
    public Unit createUnit(Unit newUnit) {
        String sql = "INSERT INTO unit (team_id, name, class, rank, species, base_cost, wounds, defense, mettle, " +
                "move, ranged, melee, strength, empty_skills, special_rules, spent_exp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        //TODO WORKING HERE
        return null;
    }



    /*
    PRIVATE METHODS
     */

    private Unit mapRowToUnit(SqlRowSet row){
        Unit newUnit = new Unit();
        newUnit.setId(row.getInt("unit_id"));
        newUnit.setId(row.getInt("team_id"));
        newUnit.setName(row.getString("name"));
        newUnit.setTitle(row.getString("class"));
        newUnit.setRank(row.getString("rank"));
        newUnit.setSpecies(row.getString("species"));
        newUnit.setBaseCost(row.getInt("base_cost"));
        newUnit.setWounds(row.getInt("wounds"));
        newUnit.setDefense(row.getInt("defence"));
        newUnit.setMettle(row.getInt("mettle"));
        newUnit.setMove(row.getInt("move"));
        newUnit.setRanged(row.getInt("ranged"));
        newUnit.setMelee(row.getInt("melee"));
        newUnit.setStrength(row.getInt("strength"));
        newUnit.setEmptySkills(row.getInt("empty_skills"));
        newUnit.setSpecialRules(row.getString("special_rules"));
        newUnit.setSpentExperience(row.getInt("spent_exp"));
        newUnit.setUnspentExperience(row.getInt("unspent_exp"));
        newUnit.setTotalAdvances(row.getInt("total_advances"));
        newUnit.setTenPointAdvances(row.getInt("ten_point_advances"));

        return newUnit;
    }

    private Unit mapRowToUnitFromUnitReference(SqlRowSet row){
        Unit newUnit = new Unit();
        newUnit.setTitle(row.getString("class"));
        newUnit.setRank(row.getString("rank"));
        newUnit.setSpecies(row.getString("species"));
        newUnit.setBaseCost(row.getInt("base_cost"));
        newUnit.setWounds(row.getInt("wounds"));
        newUnit.setDefense(row.getInt("defence"));
        newUnit.setMettle(row.getInt("mettle"));
        newUnit.setMove(row.getInt("move"));
        newUnit.setRanged(row.getInt("ranged"));
        newUnit.setMelee(row.getInt("melee"));
        newUnit.setStrength(row.getInt("strength"));
        newUnit.setEmptySkills(row.getInt("empty_skills"));
        newUnit.setSpecialRules(row.getString("special_rules"));
        newUnit.setUnspentExperience(convertStartingExp(newUnit.getRank()));
        newUnit.setAvailableSkillsets(generateAvailableSkillsets(row.getString("skillsets")));
        newUnit.setSkills(generateStartingSkills(row.getString("starting_skills")));
        return newUnit;
    }


    private Skillset mapRowToSkillset(SqlRowSet row) {
        Skillset newSkillset = new Skillset();
        newSkillset.setId(row.getInt("skillset_id"));
        newSkillset.setName(row.getString("skillset_name"));
        newSkillset.setCategory(row.getString("category"));
        return newSkillset;
    }

    private Skill mapRowToSkill(SqlRowSet row) {
        Skill newSkill = new Skill();
        newSkill.setId(row.getInt("skill_id"));
        newSkill.setSkillsetId(row.getInt("skillset_id"));
        newSkill.setName(row.getString("name"));
        newSkill.setSkillsetName(row.getString("skillset_name"));
        newSkill.setDescription(row.getString("description"));
        return newSkill;
    }

    private List<Skillset> generateAvailableSkillsets(String skillsetsAsString){
        int[] skillsetsAsArray = referenceArraySplitter(skillsetsAsString);
        List<Skillset> availableSkillsets = new ArrayList<>();
        Map<Integer, Skillset> skillsetMap = getSkillSetMap();
        for (int skillset : skillsetsAsArray){
            availableSkillsets.add(skillsetMap.get(skillset));
        }
        return availableSkillsets;
    }

    private List<Skill> generateStartingSkills(String skillsAsString){
        List<Skill> startingSkills = new ArrayList<>();
        int[] skillArray = referenceArraySplitter(skillsAsString);
        Map<Integer, Skill> skillMap = getSkillMap();
        for (int skill : skillArray){
            startingSkills.add(skillMap.get(skill));
        }
        return  startingSkills;
    }

    private Map<Integer, Skillset> getSkillSetMap(){
        Map<Integer, Skillset> skillsetMap = new HashMap<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(SELECT_ALL_FROM_SKILLSET_REFERENCE, Skillset[].class);
            while (results.next()){
                Skillset skillset = mapRowToSkillset(results);
                skillsetMap.put(skillset.getId(), skillset);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return skillsetMap;
    }

    private Map<Integer, Skill> getSkillMap(){
        Map<Integer, Skill> skillMap = new HashMap<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(SELECT_ALL_FROM_SKILL_REFERENCE, Skill[].class);
            while (results.next()){
                Skill skill = mapRowToSkill(results);
                skillMap.put(skill.getId(), skill);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return skillMap;
    }


    private int convertStartingExp(String rank){
        if (rank.equalsIgnoreCase("Rank and File")){
            return 0;
        } else if (rank.equalsIgnoreCase("Specialist")) {
            return 21;
        } else if (rank.equalsIgnoreCase("Elite")) {
            return 46;
        } else {
            return 75;
        }
    }

    private int[] referenceArraySplitter (String arrayAsString){
        if ( arrayAsString == null || arrayAsString.isEmpty()){
            return new int[0];
        } else {
            arrayAsString = arrayAsString.substring(1,arrayAsString.length()-1);
            String[] skillsetArray = arrayAsString.split("\\|");
            int[] convertedArray = new int[skillsetArray.length];
            for (int i = 0; i < convertedArray.length; i++){
                convertedArray[i] = Integer.parseInt(skillsetArray[i]);
            }
            return convertedArray;
        }
    }
}
