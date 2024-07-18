package my.TNTBuilder.dao;

import my.TNTBuilder.exception.DaoException;
import my.TNTBuilder.exception.ValidationException;
import my.TNTBuilder.model.Injury;
import my.TNTBuilder.model.Skill;
import my.TNTBuilder.model.Skillset;
import my.TNTBuilder.model.Unit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class JdbcUnitDaoTests extends BaseDaoTests{

    private JdbcUnitDao sut;

    @Before
    public void setup(){
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUnitDao(jdbcTemplate);
    }

    @After
    public void clearChanges() throws ValidationException {
        UNIT1.setName("UnitName1");
    }

    @Test
    public void getUnitById_returns_correct_unit() {
        Unit testUnit = sut.getUnitById(1,1);
        Assert.assertNotNull(testUnit);
        Assert.assertEquals(UNIT1, testUnit);
    }

    @Test (expected = DaoException.class)
    public void getUnitById_throws_exception_with_wrong_userId() {
        sut.getUnitById(1,2);
        Assert.fail();
    }

    @Test (expected = DaoException.class)
    public void getUnitById_throws_exception_with_invalid_id() {
        sut.getUnitById(99,1);
        Assert.fail();
    }

    @Test
    public void addUnitToDatabase_adds_unit() {
        Unit expectedUnit = new Unit(4, 2, "Name", "Defender", "Rank and File",
                "Human", 23,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),  new ArrayList<>());
        expectedUnit.getAvailableSkillsets().add(new Skillset(1, "Melee", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(2, "Marksmanship", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        expectedUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));

        Unit newUnit = sut.createUnit(expectedUnit);
        expectedUnit.setId(newUnit.getId());
        Unit testUnit = sut.getUnitById(newUnit.getId(), 1);
        Assert.assertNotNull(testUnit);
        Assert.assertEquals(expectedUnit, testUnit);

    }

    @Test
    public void getFactionIdByUnitId_returns_faction_id(){
        int testFactionId = sut.getFactionIdByUnitReferenceId(UNIT1.getId());
        Assert.assertEquals(1, testFactionId);
    }

    @Test(expected = DaoException.class)
    public void getFactionIdByUnitId_throws_exception_on_invalid_unit(){
        sut.getFactionIdByUnitReferenceId(50);
        Assert.fail();
    }

    @Test
    public void getListOfUnitsByFactionId_returns_correct_list(){
        Unit expectedUnit = new Unit(4, 0, "", "Defender", "Rank and File",
                "Human", 23,1,6,5,5,4,4,5,0,
                "N/A",0,0,0,0,
                new ArrayList<>(), new ArrayList<>(),  new ArrayList<>(), new ArrayList<>());
        expectedUnit.getAvailableSkillsets().add(new Skillset(1, "Melee", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(2, "Marksmanship", "Skill"));
        expectedUnit.getAvailableSkillsets().add(new Skillset(3, "Survival", "Skill"));
        expectedUnit.getSkills().add(new Skill(5, "Brave", "+2 bonus when making Will tests.",
                7, "Tenacity"));


        List<Unit> testList = sut.getListOfUnitsByFactionId(1);
        Assert.assertNotNull(testList);
        Assert.assertEquals(6, testList.size());
        Assert.assertEquals(expectedUnit, testList.get(3));
        Assert.assertEquals(10, testList.get(4).getId());
    }

    @Test
    public void getAllUnitsForTeam_returns_correct_list() {
        List<Unit> testList = sut.getAllUnitsForTeam(1);

        Assert.assertEquals(2, testList.size());
        Assert.assertEquals(UNIT1, testList.get(0));
        Assert.assertFalse(testList.contains(UNIT2));
    }

    @Test
    public void updateUnit_updates_unit() {
        Unit updatedUnit = UNIT1;
        updatedUnit.setName("Updated Name");
        sut.updateUnit(updatedUnit);
        Unit testUnit = sut.getUnitById(updatedUnit.getId(), 1);
        Assert.assertEquals(UNIT1, testUnit);

    }

    @Test
    public void getPotentialSkills_returns_correct_list_one_skillset(){
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
    public void getPotentialSkills_returns_correct_list_multiple_skillsets(){
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
    public void getPotentialSkills_does_not_return_existing_skills(){
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
    public void addSkillToUnit_adds_skill_to_unit() {
        Skill skill1 = new Skill(6, "Brute", "Gain +1 to Strength Stat when making Melee attacks. " +
                "Ignore heavy weapons rule.", 6, "Brawn");

        sut.addSkillToUnit(6, 2);
        Unit testUnit = sut.getUnitById(2, 2);
        Assert.assertEquals(1, testUnit.getSkills().size());
        Assert.assertTrue(testUnit.getSkills().contains(skill1));
    }

    @Test
    public void deleteUnit_deletes_unit() {
        sut.deleteUnit(UNIT3);
        List<Unit> testList = sut.getAllUnitsForTeam(1);
        Assert.assertEquals(1, testList.size());
        Assert.assertFalse(testList.contains(UNIT3));

    }

    @Test
    public void getPotentialInjuries_lists_all_injuries() throws ValidationException{
        List<Injury> testList = sut.getAllPotentialInjuries(UNIT1);
        Assert.assertEquals(6, testList.size());
        Assert.assertTrue(testList.contains(GASHED_LEG));
    }



}
