package my.TNTBuilder.service;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.model.Team;
import my.TNTBuilder.validator.TeamValidator;
import my.TNTBuilder.validator.UnitValidator;
import my.TNTBuilder.dao.*;
import my.TNTBuilder.exception.ServiceException;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Unit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UnitServiceTests extends BaseDaoTests {
    private UnitService sut;
    private TeamDao teamDao;
    private ItemDao itemDao;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        teamDao = new JdbcTeamDao(jdbcTemplate);
        UnitDao unitDao = new JdbcUnitDao(jdbcTemplate);
        itemDao = new JdbcItemDao(jdbcTemplate);
        sut = new UnitService(unitDao, itemDao, new UnitValidator(unitDao), new TeamService(teamDao, new TeamValidator(), jdbcTemplate));
    }

    @After
    public void resetIds() {
        ARMOR.setId(1);
        WEAPON.setId(2);
    }

    @Test
    public void createNewUnit_creates_new_unit() throws ServiceException{

        Unit expectedUnit = generateExpectedRankAndFileCaravanner();
        expectedUnit.setTeamId(1);

        Unit newUnit = sut.createNewUnit(expectedUnit, 1);
        expectedUnit.setId(newUnit.getId());

        Unit testUnit = sut.getUnitById(expectedUnit.getId(), 1);

        Assert.assertNotNull(testUnit);
        Assert.assertEquals(expectedUnit, testUnit);
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_if_team_does_not_belong_to_user() throws ServiceException{
        Unit invalidUnit = new Unit();
        invalidUnit.setId(9);
        invalidUnit.setTeamId(3);
        sut.createNewUnit(invalidUnit, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_if_team_can_not_afford_unit() throws ServiceException{

        Unit invalidUnit = new Unit();
        invalidUnit.setId(1);
        invalidUnit.setTeamId(5);
        sut.createNewUnit(invalidUnit, 4);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_if_unit_can_not_belong_to_faction() throws ServiceException{
        Unit invalidUnit = new Unit();
        invalidUnit.setId(1);
        invalidUnit.setTeamId(2);
        sut.createNewUnit(invalidUnit, 1);
        Assert.fail();
    }

    @Test
    public void createNewUnit_does_not_throw_exception_if_team_can_purchase_freelancer_units() throws ServiceException{
        Unit validUnit = new Unit();
        validUnit.setId(10);
        validUnit.setTeamId(4);

        try {
            sut.createNewUnit(validUnit, 4);
        } catch (ServiceException e){
            Assert.fail();
        }
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_if_team_tries_to_buy_leader_while_having_leader() throws ServiceException{
        Unit invalidUnit = new Unit();
        invalidUnit.setId(1);
        invalidUnit.setTeamId(1);
        sut.createNewUnit(invalidUnit, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_if_team_has_no_leader_and_does_not_buy_leader() throws ServiceException{
        Unit invalidUnit = new Unit();
        invalidUnit.setId(4);
        invalidUnit.setTeamId(6);
        sut.createNewUnit(invalidUnit, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_if_team_cannot_buy_elites() throws ServiceException{
        Unit invalidUnit = new Unit();
        invalidUnit.setId(2);
        invalidUnit.setTeamId(7);
        sut.createNewUnit(invalidUnit, 4);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void createNewUnit_throws_exception_if_team_cannot_buy_specialists() throws ServiceException{
        Unit invalidUnit = new Unit();
        invalidUnit.setId(11);
        invalidUnit.setTeamId(1);
        sut.createNewUnit(invalidUnit, 1);
        Assert.fail();
    }

    @Test
    public void getUnitsForFaction_returns_only_leader_if_team_has_no_leader() throws ServiceException{

        List<Unit> testList = sut.getUnitsForFaction(1, new Team());

        Assert.assertNotNull(testList);
        Assert.assertEquals(1, testList.size());
        Assert.assertEquals("Leader", testList.get(0).getRank());
    }

    @Test
    public void getUnitsForFaction_returns_list_of_all_non_leaders_if_team_has_leader_and_no_restrictions() throws ServiceException{
        Team team = instantiateTeamWithLeader();
        Unit expectedUnit = generateExpectedRankAndFileCaravanner();

        //Add enough units to allow both specialists and freelancers without disallowing elites
        team.getUnitList().add(UNIT2);
        team.getUnitList().add(UNIT2);
        team.getUnitList().add(expectedUnit);
        team.getUnitList().add(expectedUnit);
        team.getUnitList().add(expectedUnit);

        List<Unit> testList = sut.getUnitsForFaction(1, team);

        Assert.assertNotNull(testList);
        Assert.assertEquals(5, testList.size()); //returns all valid units
        Assert.assertEquals(expectedUnit, testList.get(2)); //unit returned is expected one
        Assert.assertEquals(10, testList.get(3).getId()); //can return freelancers
        for (Unit unit : testList){
            Assert.assertFalse("Leader".equalsIgnoreCase(unit.getRank())); //No leaders
        }
    }

    @Test
    public void getUnitsForFaction_does_not_include_freelancers_when_they_are_invalid() throws ServiceException{
        Team team = instantiateTeamWithLeader();
        Unit expectedUnit = generateExpectedRankAndFileCaravanner();
        team.getUnitList().add(expectedUnit);
        team.getUnitList().add(expectedUnit);

        List<Unit> testList = sut.getUnitsForFaction(1, team);

        Assert.assertNotNull(testList);
        Assert.assertEquals(4, testList.size()); //returns all valid units except for freelancers
        for (Unit unit : testList){
            Assert.assertFalse("Freelancer".equalsIgnoreCase(unit.getRank()));
        }
    }

    @Test
    public void getUnitsForFaction_returns_list_without_elites_if_team_has_3_elites() throws ServiceException{
        Team team = instantiateTeamWithLeader();
        team.getUnitList().add(UNIT2);
        team.getUnitList().add(UNIT2);
        team.getUnitList().add(UNIT2);

        List<Unit> testList = sut.getUnitsForFaction(1, team);

        Assert.assertNotNull(testList);
        Assert.assertEquals(3, testList.size());
        for (Unit unit : testList){
            Assert.assertFalse("Elite".equalsIgnoreCase(unit.getRank()));
        }
    }

    @Test
    public void getUnitsForFaction_returns_list_without_specialists_if_team_has_too_many_specialists() throws ServiceException{
        Team team = instantiateTeamWithLeader();
        team.getUnitList().add(UNIT2);
        team.getUnitList().add(UNIT2);
        team.getUnitList().add(UNIT3);

        List<Unit> testList = sut.getUnitsForFaction(1, team);

        Assert.assertNotNull(testList);
        Assert.assertEquals(4, testList.size());
        for (Unit unit : testList){
            Assert.assertFalse("Specialist".equalsIgnoreCase(unit.getRank()));
        }
    }

    @Test
    public void getPotentialSkills_returns_correct_list_one_skillset() throws ServiceException{
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");
        Skill skill2 = new Skill(7, "Bully", "All enemies defeated by this model in close combat are knocked prone " +
                "in addition to any other combat result.", 6, "Brawn");

        List<Skill> skillList = sut.getPotentialSkills(2);

        Assert.assertEquals(2, skillList.size());
        Assert.assertTrue(skillList.contains(skill1));
        Assert.assertTrue(skillList.contains(skill2));

    }

    @Test
    public void getPotentialSkills_returns_correct_list_multiple_skillsets() throws ServiceException{
        Skill skill1 = new Skill(3, "Reconnoiter", "At the start of the game after all models have " +
                "deployed but before init is determined make a free move action.", 4, "Quickness");
        Skill skill2 = new Skill(4, "Trekker", "When moving through Difficult Terrain attempt an " +
                "Agility test (MET/TN 10) for free. On pass move through terrain without movement penalty.",
                3, "Survival");

        List<Skill> skillList = sut.getPotentialSkills(1);

        Assert.assertEquals(2, skillList.size());
        Assert.assertTrue(skillList.contains(skill1));
        Assert.assertTrue(skillList.contains(skill2));

    }

    @Test
    public void getPotentialSkills_does_not_return_existing_skills() throws ServiceException{
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");
        Skill skill2 = new Skill(7, "Bully", "All enemies defeated by this model in close combat are knocked prone " +
                "in addition to any other combat result.", 6, "Brawn");

        List<Skill> skillList = sut.getPotentialSkills(3);

        Assert.assertEquals(1, skillList.size());
        Assert.assertTrue(skillList.contains(skill1));
        Assert.assertFalse(skillList.contains(skill2));

    }

    @Test
    public void addSkillToUnit_adds_skill_to_unit() throws ServiceException{
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");
        sut.addSkillToUnit(skill1, 2, 2);
        Unit testUnit = sut.getUnitById(2, 2);
        Assert.assertEquals(1, testUnit.getSkills().size());
        Assert.assertTrue(testUnit.getSkills().contains(skill1));
    }

    @Test (expected = ServiceException.class)
    public void addSkillToUnit_throws_exception_if_user_does_not_own_unit() throws ServiceException{
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");
        sut.addSkillToUnit(skill1, 2, 1);
        Assert.fail();
    }

    @Test (expected = ServiceException.class)
    public void addSkillToUnit_throws_exception_if_unit_cannot_have_skill() throws ServiceException{
        Skill skill1 = new Skill(1, "Illegal Skill", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 3, "Brawn");
        sut.addSkillToUnit(skill1, 2, 2);
        Assert.fail();
    }

    @Test
    public void getUnitById_returns_correct_unit() throws ServiceException{
        Unit testUnit = sut.getUnitById(1,1);
        Unit expected = UNIT1;
        Assert.assertNotNull(testUnit);
        Assert.assertEquals(UNIT1, testUnit);
    }

    @Test (expected = ServiceException.class)
    public void getUnitById_throws_exception_incorrect_user() throws ServiceException{
        sut.getUnitById(1,2);
        Assert.fail();
    }

    @Test
    public void getReferenceUnitByClass_returns_expected_unit() throws ServiceException{
        Unit expectedUnit = new Unit (0, 0, "", "Raider", "Rank and File",
                "Human", 20, 1, 6, 5, 5, 4, 4, 5,
                0, "N/A", 0, 0, 0, 0,
                Arrays.asList(
                        new Skillset(1, "Melee", "Skill"),
                        new Skillset(2, "Marksmanship", "Skill"),
                        new Skillset(3, "Survival", "Skill")),
                new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());
        Unit testUnit = sut.getReferenceUnitByClass("Raider");
        Assert.assertEquals(expectedUnit, testUnit);
    }

    @Test(expected = ServiceException.class)
    public void getReferenceUnitByClass_throws_error_for_invalid_classes() throws ServiceException{
        sut.getReferenceUnitByClass("Not A Real Class");
        Assert.fail();
    }

    @Test
    public void updateUnit_updates_unit_with_valid_change() throws ServiceException{
        Unit unitToUpdate = sut.getUnitById(1,1);
        unitToUpdate.setName("New Name");

        sut.updateUnit(unitToUpdate, 1);
        Unit testUnit = sut.getUnitById(1, 1);
        Assert.assertEquals(unitToUpdate, testUnit);
    }

    @Test (expected = ServiceException.class)
    public void updateUnit_throws_exception_with_invalid_change() throws ServiceException{
        Unit unitToUpdate = sut.getUnitById(1,1);
        unitToUpdate.setUnitClass("New Class Name");
        sut.updateUnit(unitToUpdate, 1);
        Assert.fail();
    }

    @Test
    public void deleteUnit_deletes_unit_no_items() throws ServiceException{
        sut.deleteUnit(3, 1, false);
        Team testTeam = teamDao.getTeamById(1, 1);

        Assert.assertEquals(1, testTeam.getUnitList().size());
        Assert.assertFalse(testTeam.getUnitList().contains(UNIT3));
    }

    @Test
    public void deleteUnit_deletes_unit_with_items_delete_mode() throws ServiceException{
        sut.deleteUnit(1, 1, true);
        Team testTeam = teamDao.getTeamById(1, 1);

        Assert.assertEquals(1, testTeam.getUnitList().size());
        Assert.assertFalse(testTeam.getUnitList().contains(UNIT1));
        Assert.assertEquals(3, testTeam.getInventory().size());
    }

    @Test
    public void deleteUnit_deletes_unit_with_items_transfer_mode() throws ServiceException{
        sut.deleteUnit(1, 1, false);
        Team testTeam = teamDao.getTeamById(1, 1);

        Assert.assertEquals(1, testTeam.getUnitList().size());
        Assert.assertFalse(testTeam.getUnitList().contains(UNIT1));
        Assert.assertEquals(6, testTeam.getInventory().size());
        Assert.assertTrue(testTeam.getInventory().contains(ARMOR));
    }

    @Test
    public void deleteUnit_deletes_unit_without_transferring_weapon_if_freelancer() throws ServiceException {

        int itemId = itemDao.addItemToUnit(5, 10);
        WEAPON.setId(itemId);
        itemId = itemDao.addItemToUnit(1, 10);
        ARMOR.setId(itemId);

        sut.deleteUnit(10,4, false);

        Team testTeam = teamDao.getTeamById(7, 4);
        Assert.assertEquals(5, testTeam.getUnitList().size());
        Assert.assertTrue(testTeam.getInventory().contains(ARMOR));
        Assert.assertFalse(testTeam.getInventory().contains(WEAPON));

    }

    @Test
    public void getPotentialInjuries_lists_all_injuries_for_uninjured_unit() throws ServiceException{
        List<Skill> testList = sut.getPotentialInjuries(UNIT1.getId(), 1);
        Assert.assertEquals(2, testList.size());
        Assert.assertTrue(testList.contains(BANGED_HEAD));
    }

    @Test (expected = ServiceException.class)
    public void getPotentialInjuries_throws_exception_user_does_not_own_unit() throws ServiceException{
        List<Skill> testList = sut.getPotentialInjuries(UNIT1.getId(), 2);
        Assert.fail();
    }


    /*
    Private Helper Methods
     */
    private Unit generateExpectedRankAndFileCaravanner() {
        Unit expectedUnit = new Unit(4, 0, "", "Defender", "Rank and File",
                "Human", 23,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());
        expectedUnit.getAvailableSkillsets().add(new Skillset(1, "Melee", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(2, "Marksmanship", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        expectedUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));

        return expectedUnit;
    }

    private Team instantiateTeamWithLeader(){

        return new Team(1, 1, "", "Caravanners", 1, 500,
                new ArrayList<>(List.of(UNIT1)), new ArrayList<>());
    }
}
