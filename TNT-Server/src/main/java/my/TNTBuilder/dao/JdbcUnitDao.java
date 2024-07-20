package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.Injury;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Unit;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.relational.core.sql.In;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class JdbcUnitDao implements UnitDao{
    private final int FREELANCER_FACTION_ID = 7;

    private final JdbcTemplate jdbcTemplate;
    private final String SELECT_ALL_FROM_UNIT_REFERENCE = "SELECT unit_ref_id, faction_id, class, rank, species, base_cost, " +
            "wounds, defense, mettle, move, ranged, melee, strength, skillsets, starting_skills, starting_free_skills, " +
            "special_rules FROM unit_reference ";
    private final String SELECT_ALL_FROM_UNIT = "SELECT u.team_id, unit_id, name, class, rank, species, base_cost, wounds, " +
            "defense, mettle, move, ranged, melee, strength, empty_skills, special_rules, spent_exp, unspent_exp, " +
            "total_advances, ten_point_advances, u.team_id, is_banged_up, is_long_recovery FROM unit u " +
            "JOIN team t on t.team_id = u.team_id ";
    private final String SELECT_ALL_FROM_SKILLSET_REFERENCE = "SELECT ssr.skillset_id, skillset_name, category " +
            "FROM skillset_reference ssr ";
    private final String SELECT_ALL_FROM_SKILL_REFERENCE = "SELECT sr.skill_id AS skill_id, sr.skillset_id, name, description, " +
            "skillset_name FROM skill_reference sr " +
            "JOIN skillset_reference ssr ON ssr.skillset_id = sr.skillset_id ";
    private final String SELECT_ALL_FROM_INJURY_REFERENCE = "SELECT injury_id, name, description, is_stat_damage, stat_damaged, " +
            "is_removeable, is_stackable FROM injury_reference ";

    /*
    Constructor
     */
    public JdbcUnitDao (JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    /*
    PUBLIC METHODS
     */
    @Override
    public Unit getUnitById(int id, int userId) throws DaoException{
        String sql = SELECT_ALL_FROM_UNIT + "WHERE unit_id = ? AND user_id = ?";
        Unit unit = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, userId);
            while (results.next()){
                unit = mapRowToUnit(results);
            }

            if (unit == null) {
                throw new DaoException("Unable to retrieve unit. Either does not exist or does not belong to user.");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }

        return unit;
    }

    @Override
    public List<Unit> getAllUnitsForTeam(int teamId) throws DaoException{
        String sql = SELECT_ALL_FROM_UNIT + "WHERE t.team_id = ? ORDER BY base_cost desc, unit_id";
        List<Unit> unitList = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, teamId);
            while (results.next()){

                Unit unit = mapRowToUnit(results);
                unitList.add(unit);

            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return unitList;
    }

    @Override
    public Unit createUnit(Unit clientUnit) throws DaoException{
        Unit newUnit = initializeNewUnit(clientUnit);
        String sql = "INSERT INTO unit (team_id, name, class, rank, species, base_cost, wounds, defense, mettle, " +
                "move, ranged, melee, strength, empty_skills, special_rules, spent_exp) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING unit_id";
        try {
            int unitId = jdbcTemplate.queryForObject(sql, Integer.class, newUnit.getTeamId(), newUnit.getName(),
                    newUnit.getUnitClass(), newUnit.getRank(),  newUnit.getSpecies(), newUnit.getBaseCost(),
                    newUnit.getWounds(), newUnit.getDefense(), newUnit.getMettle(), newUnit.getMove(),
                    newUnit.getRanged(), newUnit.getMelee(), newUnit.getStrength(), newUnit.getEmptySkills(),
                    newUnit.getSpecialRules(), newUnit.getSpentExperience());
            newUnit.setId(unitId);
            addSkillsToUnitSkillJoinTable(unitId, newUnit.getSkills());
            addSkillsetsToUnitSkillsetJoinTable(unitId, newUnit.getAvailableSkillsets());
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Invalid data provided, cannot create unit", e);
        }
        return newUnit;
    }

    @Override
    public int getFactionIdByUnitReferenceId(int referenceUnitId) throws DaoException{
        String sql = "SELECT faction_id FROM unit_reference WHERE unit_ref_id = ?";
        int id = 0;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, referenceUnitId);
            while(results.next()){
                id = results.getInt("faction_id");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }

        if (id == 0){
            throw new DaoException("Unable to locate faction");
        }
        return id;
    }

    @Override
    public List<Unit> getListOfUnitsByFactionId(int factionId) throws DaoException {
        String sql = "SELECT unit_ref_id FROM unit_reference WHERE faction_id = ? OR faction_id = " + FREELANCER_FACTION_ID;
        List<Unit> unitList = new ArrayList<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, factionId);
            List<Integer> validUnitIds = new ArrayList<>();
            while (results.next()){
                validUnitIds.add(results.getInt("unit_ref_id"));
            }
            for (int unitRefId : validUnitIds){
                Unit unit = convertReferenceUnitToUnit(unitRefId);
                unit.setId(unitRefId);
                unitList.add(unit);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return unitList;
    }

    @Override
    public void  deleteUnit(Unit unit) throws DaoException{
        String deleteSkillSql = "DELETE FROM unit_skill WHERE skill_id = ?";
        String deleteSkillsetSql = "DELETE FROM unit_skillset WHERE skillset_id = ?";
        String deleteInjurySql = "DELETE from unit_injury WHERE injury_id = ?";
        String deleteUnitSql = "DELETE FROM unit WHERE unit_id = ?";

        try {
            for (Skill skill : unit.getSkills()){
                jdbcTemplate.update(deleteSkillSql, skill.getId());
            }
            for (Skillset skillset : unit.getAvailableSkillsets()){
                jdbcTemplate.update(deleteSkillsetSql, skillset.getId());
            }
            for (Injury injury : unit.getInjuries()){
                jdbcTemplate.update(deleteInjurySql, injury.getId());
            }


            int rowsAffected = jdbcTemplate.update(deleteUnitSql, unit.getId());
            if (rowsAffected != 1) {
                throw new DaoException("Error: Incorrect number of rows deleted");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation, unable to delete unit.", e);
        }
    }

    @Override
    public void updateUnit(Unit updatedUnit) throws DaoException{
        String sql = "UPDATE unit SET name = ?, rank = ?, wounds = ?, defense = ?, mettle = ?, move = ?, " +
                "ranged = ?, melee = ?, strength = ?, empty_skills = ?, spent_exp = ?, unspent_exp = ?, " +
                "total_advances = ?, ten_point_advances = ?, is_banged_up = ?, is_long_recovery = ? " +
                "WHERE unit_id = ?";
        try{
            int rowsAffected = jdbcTemplate.update(sql, updatedUnit.getName(), updatedUnit.getRank(), updatedUnit.getWounds(),
                    updatedUnit.getDefense(), updatedUnit.getMettle(), updatedUnit.getMove(), updatedUnit.getRanged(),
                    updatedUnit.getMelee(), updatedUnit.getStrength(), updatedUnit.getEmptySkills(),
                    updatedUnit.getSpentExperience(), updatedUnit.getUnspentExperience(), updatedUnit.getTotalAdvances(),
                    updatedUnit.getTenPointAdvances(), updatedUnit.isBangedUp(), updatedUnit.isLongRecovery(),
                    updatedUnit.getId());
            if (rowsAffected != 1){
                throw new DaoException("Incorrect number of rows affected");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Invalid data provided, cannot update unit", e);
        }
    }

    @Override
    public Unit convertReferenceUnitToUnit(int referenceUnitId) throws DaoException{
        Unit referenceUnit = null;
        String sql = SELECT_ALL_FROM_UNIT_REFERENCE + "WHERE unit_ref_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, referenceUnitId);
            while (results.next()){
                referenceUnit = mapRowToUnitFromUnitReference(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return referenceUnit;
    }

    @Override
    public Unit convertReferenceUnitToUnit(String unitClass) throws DaoException {
        Unit referenceUnit = null;
        String sql = SELECT_ALL_FROM_UNIT_REFERENCE + "WHERE class = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, unitClass);
            while (results.next()){
                referenceUnit = mapRowToUnitFromUnitReference(results);
            }

            if (referenceUnit == null){
                throw new DaoException("Unable to find reference unit matching the given class name.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return referenceUnit;
    }

    @Override
    public List<Skill> getPotentialSkills(int unitId) throws DaoException {
        List<Integer> skillsetIds = new ArrayList<>();
        List<Skill> potentialSkills = new ArrayList<>();

        String getSkillsets = "SELECT skillset_id FROM unit_skillset WHERE unit_id = ?";

        String sql = SELECT_ALL_FROM_SKILL_REFERENCE + " WHERE sr.skillset_id = ?";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(getSkillsets, unitId);
            while(results.next()){
                skillsetIds.add(results.getInt("skillset_id"));
            }

            for (int id : skillsetIds){
                results = jdbcTemplate.queryForRowSet(sql, id);
                while (results.next()){
                    Skill skill = mapRowToSkill(results);
                    potentialSkills.add(skill);
                }
            }

            List<Skill> currentSkills = getUnitSkills(unitId);
            for (Skill skill : currentSkills){
                potentialSkills.remove(skill);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }

        return potentialSkills;
    }

    @Override
    public void addSkillToUnit(int skillId, int unitId)  throws DaoException{
        String sql = "INSERT INTO unit_skill (unit_id, skill_id) VALUES (?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql, unitId, skillId);
            if (rowsAffected != 1){
                throw new DaoException("Error, " + rowsAffected + " rows affected.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
    }


    @Override
    public List<Injury> getAllPotentialInjuries(Unit unit) throws DaoException{
        String sql = SELECT_ALL_FROM_INJURY_REFERENCE;
        List<Injury> potentialInjuries = new ArrayList<>();

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while(results.next()){
                Injury injury = mapRowToInjury(results);
                potentialInjuries.add(injury);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return potentialInjuries;

    }

    @Override
    public void addInjuryToUnit(int injuryId, int unitId) throws DaoException {
        String sql = "INSERT INTO unit_injury (unit_id, injury_id) VALUES (?, ?)";
        try {
            int rowsAffected = jdbcTemplate.update(sql, unitId, injuryId);
            if (rowsAffected != 1){
                throw new DaoException("Error, " + rowsAffected + " rows affected.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Invalid data provided, cannot add injury", e);
        }

    }

    @Override
    public void deleteInjuryFromUnit(int injuryId, int unitId) throws DaoException {
        String sql = "DELETE FROM unit_injury WHERE unit_id = ? AND injury_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, unitId, injuryId);
            if (rowsAffected != 1){
                throw new DaoException("Error, " + rowsAffected + " rows affected.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Invalid data provided, cannot delete injury", e);
        }
    }

    @Override
    public void updateInjuryCount(int injuryId, int unitId, int count) throws DaoException {
        String sql = "UPDATE unit_injury SET count = ? WHERE unit_id = ? AND injury_id = ?";
        try {
            int rowsAffected = jdbcTemplate.update(sql, count, unitId, injuryId);
            if (rowsAffected != 1){
                throw new DaoException("Error, " + rowsAffected + " rows affected.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Invalid data provided, cannot update injury", e);
        }
    }


    @Override
    public Injury selectInjuryById(int injuryId) throws DaoException{
        String sql = SELECT_ALL_FROM_INJURY_REFERENCE + "WHERE injury_id = ?";
        Injury injury = null;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, injuryId);
            while(results.next()){
                injury = mapRowToInjury(results);
            }

            if (injury == null){
                throw new DaoException("This injury does not exist.");
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }

        return injury;
    }

    /*
    PRIVATE METHODS
     */
    private List<Skill> getUnitSkills(int unitId){
        List<Skill> skills = new ArrayList<>();
        String sql = SELECT_ALL_FROM_SKILL_REFERENCE + "JOIN unit_skill us ON us.skill_id = sr.skill_id " +
                "WHERE unit_id = ? " +
                "ORDER BY sr.skill_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, unitId);
        while (results.next()){
            skills.add(mapRowToSkill(results));
        }
        return skills;
    }

    private List<Skillset> getAvailableSkillsets(int unitId){
        List<Skillset> skillsets = new ArrayList<>();
        String sql = SELECT_ALL_FROM_SKILLSET_REFERENCE + "JOIN unit_skillset uss ON uss.skillset_id = ssr.skillset_id "
                + " WHERE unit_id = ? ORDER BY ssr.skillset_id";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, unitId);
        while (results.next()){
            skillsets.add(mapRowToSkillset(results));
        }
        return skillsets;
    }
    private void addSkillsToUnitSkillJoinTable(int unitId, List<Skill> skills){
        String sql = "INSERT INTO unit_skill (unit_id, skill_id) VALUES (?, ?)";
        List<Object[]> batch = new ArrayList<>();
        for (Skill skill : skills) {
            Object[] values = new Object[] { unitId, skill.getId() };
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }
    private void addSkillsetsToUnitSkillsetJoinTable(int unitId, List<Skillset> skillsets){
        String sql = "INSERT INTO unit_skillset (unit_id, skillset_id) VALUES (?, ?)";
        List<Object[]> batch = new ArrayList<>();
        for (Skillset skillset : skillsets){
            Object[] values = new Object[] { unitId, skillset.getId() };
            batch.add(values);
        }
        jdbcTemplate.batchUpdate(sql, batch);
    }
    private Unit initializeNewUnit(Unit providedUnit)  throws DaoException{
        Unit newUnit = convertReferenceUnitToUnit(providedUnit.getId());
        if (newUnit == null){
            throw new DaoException("Invalid unit provided, cannot create unit.");
        }
        newUnit.setTeamId(providedUnit.getTeamId());
        newUnit.setName(providedUnit.getName());
        return newUnit;
    }
    private Unit mapRowToUnitFromUnitReference(SqlRowSet row) throws DaoException{
        Unit newUnit = new Unit();
        newUnit.setUnitClass(row.getString("class"));
        newUnit.setRank(row.getString("rank"));
        newUnit.setSpecies(row.getString("species"));
        newUnit.setBaseCost(row.getInt("base_cost"));
        newUnit.setWounds(row.getInt("wounds"));
        newUnit.setDefense(row.getInt("defense"));
        newUnit.setMettle(row.getInt("mettle"));
        newUnit.setMove(row.getInt("move"));
        newUnit.setRanged(row.getInt("ranged"));
        newUnit.setMelee(row.getInt("melee"));
        newUnit.setStrength(row.getInt("strength"));
        newUnit.setEmptySkills(row.getInt("starting_free_skills"));
        newUnit.setSpecialRules(row.getString("special_rules"));
        newUnit.setSpentExperience(convertStartingExp(newUnit.getRank()));
        newUnit.setAvailableSkillsets(convertAvailableSkillsets(row.getString("skillsets")));
        newUnit.setSkills(convertStartingSkills(row.getString("starting_skills")));
        return newUnit;
    }
    private int convertStartingExp(String rank){
        if (rank.equalsIgnoreCase("Rank and File")){
            return 0;
        } else if (rank.equalsIgnoreCase("Specialist")) {
            return 21;
        } else if (rank.equalsIgnoreCase("Elite") || rank.equalsIgnoreCase("Freelancer")) {
            return 46;
        } else {
            return 75;
        }
    }
    private List<Skillset> convertAvailableSkillsets(String skillsetsAsString) throws DaoException{
        int[] skillsetsAsArray = referenceArraySplitter(skillsetsAsString);
        List<Skillset> availableSkillsets = new ArrayList<>();
        Map<Integer, Skillset> skillsetMap = generateSkillSetMap();
        for (int skillset : skillsetsAsArray){
            availableSkillsets.add(skillsetMap.get(skillset));
        }
        return availableSkillsets;
    }
    private List<Skill> convertStartingSkills(String skillsAsString) throws DaoException{
        List<Skill> startingSkills = new ArrayList<>();
        int[] skillArray = referenceArraySplitter(skillsAsString);
        Map<Integer, Skill> skillMap = generateSkillMap();
        for (int skill : skillArray){
            startingSkills.add(skillMap.get(skill));
        }
        return  startingSkills;
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
    private Map<Integer, Skillset> generateSkillSetMap() throws DaoException{
        Map<Integer, Skillset> skillsetMap = new HashMap<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(SELECT_ALL_FROM_SKILLSET_REFERENCE);
            while (results.next()){
                Skillset skillset = mapRowToSkillset(results);
                skillsetMap.put(skillset.getId(), skillset);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return skillsetMap;
    }
    private Map<Integer, Skill> generateSkillMap() throws DaoException{
        Map<Integer, Skill> skillMap = new HashMap<>();
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(SELECT_ALL_FROM_SKILL_REFERENCE);
            while (results.next()) {
                Skill skill = mapRowToSkill(results);
                skillMap.put(skill.getId(), skill);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        }
        return skillMap;
    }
    private Unit mapRowToUnit(SqlRowSet row)  throws DaoException{
        Unit newUnit = new Unit();
        newUnit.setId(row.getInt("unit_id"));
        newUnit.setTeamId(row.getInt("team_id"));
        newUnit.setName(row.getString("name"));
        newUnit.setUnitClass(row.getString("class"));
        newUnit.setRank(row.getString("rank"));
        newUnit.setSpecies(row.getString("species"));
        newUnit.setBaseCost(row.getInt("base_cost"));
        newUnit.setWounds(row.getInt("wounds"));
        newUnit.setDefense(row.getInt("defense"));
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
        newUnit.setBangedUp(row.getBoolean("is_banged_up"));
        newUnit.setLongRecovery(row.getBoolean("is_long_recovery"));

        newUnit.setSkills(getUnitSkills(newUnit.getId()));

        newUnit.setAvailableSkillsets(getAvailableSkillsets(newUnit.getId()));
        newUnit.setInjuries(getAllInjuriesOnUnit(newUnit.getId()));

        ItemDao itemDao = new JdbcItemDao(jdbcTemplate);
        newUnit.setInventory(itemDao.getAllItemsForUnit(newUnit.getId()));

        return newUnit;
    }

    private List<Injury> getAllInjuriesOnUnit(int unitId) throws DaoException {
        String sql = "SELECT ir.injury_id, name, description, is_stat_damage, stat_damaged, is_removeable, " +
                "is_stackable, count FROM injury_reference ir " +
                "JOIN unit_injury ui ON ui.injury_id = ir.injury_id "+
                "WHERE unit_id = ?";
        List<Injury> injuries = new ArrayList<>();

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, unitId);
            while(results.next()){
                Injury injury = mapRowToInjury(results);
                injury.setCount(results.getInt("count"));
                injuries.add(injury);
            }

        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to database", e);
        } catch (ValidationException e){
            throw new DaoException(e.getMessage());
        }

        return injuries;
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

    private Injury mapRowToInjury(SqlRowSet row) {
        Injury newInjury = new Injury();
        newInjury.setId(row.getInt("injury_id"));
        newInjury.setName(row.getString("name"));
        newInjury.setDescription(row.getString("description"));
        newInjury.setStatDamage(row.getBoolean("is_stat_damage"));
        newInjury.setStatDamaged(row.getString("stat_damaged"));
        newInjury.setRemoveable(row.getBoolean("is_removeable"));
        newInjury.setStackable(row.getBoolean("is_stackable"));
        return newInjury;
    }



}
